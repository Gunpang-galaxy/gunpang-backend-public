package com.galaxy.gunpang.develop;

import com.galaxy.gunpang.avatar.batch.AvatarScheduler;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.dto.AvatarAddReqDto;
import com.galaxy.gunpang.avatar.model.enums.Cause;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.avatar.service.AvatarService;
import com.galaxy.gunpang.goal.model.dto.GoalReqDto;
import com.galaxy.gunpang.goal.service.GoalService;
import com.galaxy.gunpang.user.model.dto.LogInResDto;
import com.galaxy.gunpang.user.model.dto.SignUpReqDto;
import com.galaxy.gunpang.user.service.JwtService;
import com.galaxy.gunpang.user.service.RedisService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Tag(name = "Develop", description = "개발자 테스트용 API")
@Slf4j
@RestController
@RequestMapping("/develop")
@RequiredArgsConstructor
public class DevelopController {
    private final JobLauncher jobLauncher;
    private final Job damageJob;
    private final Job levelUpJob;
    private final UserService userService;
    private final AvatarService avatarService;
    private final GoalService goalService;
    private final JwtService jwtService;
    private final RedisService redisService;

    private final AvatarRepository avatarRepository;
    private final DeathCauseRepository deathCauseRepository;

    private final AvatarScheduler avatarScheduler;

//    @Operation(summary = "매일 실행되는 scheduler job 실행", description = "매일 0시에 실행되는 job 실행")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "요청 성공")
//            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
//    })
//    @PostMapping("/avatar/daily")
//    public ResponseEntity<?> daily(){
//        log.debug("[POST] daily method");
//
//        avatarScheduler.daily();
//
//        return ResponseEntity.ok().build();
//    }
    @Operation(summary = "아바타 체력 변화", description = "ALIVE 상태의 전체 아바타에 로직 실행")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/avatar/hp")
    public ResponseEntity<?> hp(){
        log.debug("[POST] hp method");

        avatarScheduler.damage();

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아바타 레벨업 & 졸업", description = "살아있는 일주일이 지난 아바타에 대해 로직 실행")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/avatar/level-up")
    public ResponseEntity<?> levelUp(){
        log.debug("[POST] levelUp method");

        avatarScheduler.levelUp();

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "테스트 데이터 넣기", description = "유저, 아바타, 목표 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = LogInResDto.class)))
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/test-data")
    public ResponseEntity<?> data(){
        log.debug("[PUT] hp method");

        String randomString = new RandomString(8).nextString();

        String googleId = userService.addUser(SignUpReqDto.builder()
                .googleId(randomString)
                .birthYear(1996)
                .email("pang" + randomString + "@google.com")
                .height(180)
                .gender("MALE")
                .build()).getGoogleId();

        Long userId = userService.getIdByGoogleId(googleId).getId();

        avatarService.addRandomAvatar(userId, new AvatarAddReqDto("AVATAR_CAT", "팡이"));

        goalService.addGoal(userId, new GoalReqDto(11, 30, 6,30, 127, 30));

        LogInResDto logInResDto = jwtService.createTokens(googleId);
        redisService.updateToken(logInResDto);

        return ResponseEntity.ok().body(logInResDto);
    }


    @Operation(summary = "테스트 다수 데이터 넣기", description = "유저, 아바타, 목표 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = LogInResDto.class)))
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/test-data-multiple")
    public ResponseEntity<?> multipleData(){
        log.debug("[Post] multipleData method");

        int num = new Random().nextInt(100);
        for(int i = 0; i < 100 + num; i++){
            data();
        }
        for(int i = 0; i < 100 + num; i++) {
            nData(7);
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "테스트 n일전 데이터 넣기", description = "유저, 아바타, 목표 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = LogInResDto.class)))
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/test-n-data")
    public ResponseEntity<?> nData(@RequestParam int n){
        log.debug("[POST] nData method {}", n);

        String randomString = new RandomString(8).nextString();

        String googleId = userService.addUser(SignUpReqDto.builder()
                .googleId(randomString)
                .birthYear(1996)
                .email("pang" + randomString + "@google.com")
                .height(180)
                .gender("MALE")
                .build()).getGoogleId();

        Long userId = userService.getIdByGoogleId(googleId).getId();

        avatarService.addWithBefore(userId, new AvatarAddReqDto("AVATAR_CAT", "팡이"), n);

        goalService.addGoal(userId, new GoalReqDto(11, 30, 6,30, 127, 30));

        LogInResDto logInResDto = jwtService.createTokens(googleId);
        redisService.updateToken(logInResDto);

        return ResponseEntity.ok().body(logInResDto);
    }

    @Operation(summary = "그 달에 더미 데이터 넣기", description = "그 달에 데미지 데이터를 랜덤 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/dummyDamage")
    public ResponseEntity<?> monthDummyDamageData(@RequestHeader("Authorization") String token, @RequestParam int year, @RequestParam int month){
        log.debug("[POST] dummyDamage method : {}, {}", year, month);

        Long userId = userService.getIdByToken(token).getId();

        Long avatarId = avatarRepository.getCurIdByUserId(userId).get();
        Avatar avatar = avatarRepository.findById(avatarId).get();

        List<DeathCause> deathCauses = new ArrayList<>();

        // 랜덤 생성
        for(LocalDate date = LocalDate.of(year, month, 1); date.getMonthValue() == month; date = date.plusDays(1)){
            int cnt = new Random().nextInt(4);
            for(int i = 0; i < cnt; i++){
                deathCauses.add(DeathCause.builder().cause(Cause.values()[i]).date(date).avatar(avatar).build());
            }
        }
        deathCauseRepository.saveAll(deathCauses);

        return ResponseEntity.ok().build();
    }
}
