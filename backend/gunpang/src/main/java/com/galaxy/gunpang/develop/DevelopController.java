package com.galaxy.gunpang.develop;

import com.galaxy.gunpang.avatar.batch.AvatarScheduler;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.dto.AvatarAddReqDto;
import com.galaxy.gunpang.avatar.model.enums.Cause;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.avatar.service.AvatarService;
import com.galaxy.gunpang.dailyRecord.exception.FoodRecordTimeBadRequestException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.model.dto.ExerciseRecordReqDto;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import com.galaxy.gunpang.exercise.repository.ExerciseRepository;
import com.galaxy.gunpang.goal.model.dto.GoalReqDto;
import com.galaxy.gunpang.goal.service.GoalService;
import com.galaxy.gunpang.user.annotation.NoAuth;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Tag(name = "Develop", description = "개발자 테스트용 API")
@Slf4j
@RestController
@RequestMapping("/develop")
@RequiredArgsConstructor
public class DevelopController {
    private final JobLauncher jobLauncher;
    private final UserService userService;
    private final AvatarService avatarService;
    private final GoalService goalService;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final DailyRecordService dailyRecordService;
    private final DailyRecordRepository dailyRecordRepository;
    private final ExerciseRepository exerciseRepository;

    private final AvatarRepository avatarRepository;
    private final DeathCauseRepository deathCauseRepository;

    private final AvatarScheduler avatarScheduler;

    @NoAuth
    @Operation(summary = "아바타 체력 변화", description = "ALIVE 상태의 전체 아바타에 로직 실행")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/avatar/daily")
    public ResponseEntity<?> daily(){
        log.debug("[POST] daily method");

        avatarScheduler.daily();

        return ResponseEntity.ok().build();
    }

    @NoAuth
    @Operation(summary = "테스트 n일전 기록 생성", description = "n일전 하루 기록을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "하루 기록 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "하루 기록 생성 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/createNRecord")
    public ResponseEntity<?> createRecord(@RequestHeader("Authorization") String token, @RequestParam int n) throws Exception {
        Long userId = userService.getIdByToken(token).getId();

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setRecordDate(LocalDate.now().minusDays(n));
        dailyRecord.setUserId(userId);
        dailyRecordRepository.save(dailyRecord);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @NoAuth
    @Operation(summary = "테스트 n일전 수면 기록", description = "n일전 수면 기록수동으로 저장하는 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수면 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "수면 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/sleep")
    public ResponseEntity<?> recordSleep(@RequestHeader("Authorization") String token, @RequestBody SleepRecordReqDto sleepRecordReqDto, @RequestParam int n) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        // 1. 오늘 날짜에 맞는 하루 기록 가져오기
        LocalDate today = LocalDate.now().minusDays(n);
        DailyRecord dailyRecord = dailyRecordService.returnDailyRecordOfDate(userId, today);

        // 2. 입력값 LocalTime으로 수정
        LocalTime sleepStartTime = LocalTime.of(sleepRecordReqDto.getSleepStartHour(), sleepRecordReqDto.getSleepStartMinute());
        LocalTime sleepEndTime = LocalTime.of(sleepRecordReqDto.getSleepEndHour(), sleepRecordReqDto.getSleepEndMinute());

        // 3. LocalDateTime 형식으로 수면 시간 수정
        LocalDateTime sleepStartAt = LocalDateTime.now();
        sleepStartAt = sleepStartAt.with(sleepStartTime).with(today);
        LocalDateTime sleepEndAt = LocalDateTime.now();
        sleepEndAt = sleepEndAt.with(sleepEndTime).with(today);

        // 어제 자고 오늘 일어난 경우
        if (sleepStartAt.isAfter(sleepEndAt)) {
            sleepStartAt = sleepStartAt.minusDays(1);
        }

        // 4. 하루 기록 수면 시각, 기상 시각 업데이트
        dailyRecord.setSleepRecord(sleepStartAt, sleepEndAt);
        dailyRecordRepository.save(dailyRecord);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @NoAuth
    @Operation(summary = "테스트 n일전 식사 기록", description = "n일전 식사한 기록을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식사 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "식사 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/food")
    public ResponseEntity<?> recordFood(@RequestHeader("Authorization") String token, @RequestBody FoodType foodType, @RequestParam int hour, @RequestParam int n) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        //오늘 날짜에 맞는 하루 기록 가져오기
        //언제 밥먹었은지보고 하루 기록에 기록 + 저장

        LocalDate today = LocalDate.now().minusDays(n);

        DailyRecord dailyRecord = dailyRecordService.returnDailyRecordOfDate(userId, today);

        if (5 <= hour && hour <= 10) {
            //이미 기록이 있을 시에는 기록 덮어쓰기
            dailyRecord.setBreakfastFoodType(foodType);
        }
        if (11 <= hour && hour <= 16) {
            dailyRecord.setLunchFoodType(foodType);
        }
        if (17 <= hour && hour <= 21) {
            dailyRecord.setDinnerFoodType(foodType);
        }
        if (22 <= hour || hour <= 4) {
            throw new FoodRecordTimeBadRequestException(hour);
        }

        dailyRecordRepository.save(dailyRecord);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @NoAuth
    @Operation(summary = "테스트 n일전 운동 기록", description = "n일전 운동한 기록을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "운동 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "운동 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> recordExercise(@RequestHeader("Authorization") String token, @RequestBody ExerciseRecordReqDto exerciseRecordReqDto, @RequestParam int n) throws Exception {
        Long userId = userService.getIdByToken(token).getId();

        LocalDateTime startedTime = exerciseRecordReqDto.getStartedTime();
        LocalDateTime finishedTime = exerciseRecordReqDto.getFinishedTime();
        ExerciseIntensity exerciseIntensity = exerciseRecordReqDto.getExerciseIntensity();

        //정보들을 db에 넣어야함
        LocalDate today = LocalDate.now().minusDays(n);

        //운동 강도, 시작 시간, 종료 시간 등록
        //만약 오늘 날짜로 하루 기록이 있다면 있는 것과 연결
        DailyRecord dailyRecord = dailyRecordService.returnDailyRecordOfDate(userId, today);
        //운동 시간 계산
        long exerciseAccTime = Duration.between(startedTime, finishedTime).toSeconds();

        dailyRecord.setExerciseAccTime(dailyRecord.getExerciseAccTime()+exerciseAccTime);
        //하루 기록 업데이트
        dailyRecordRepository.save(dailyRecord);
        //운동 저장
        Exercise exercise = new Exercise(dailyRecord.getId(), exerciseIntensity, startedTime, finishedTime);
        exerciseRepository.save(exercise);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @NoAuth
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

    @NoAuth
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

    @NoAuth
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

    @NoAuth
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

    @NoAuth
    @Operation(summary = "시간 체크", description = "현재 시각을 체크한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping("/check-time")
    public ResponseEntity<String> checkTime(){
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        log.debug("[GET] checkTime method : {} {}", localDate, localDateTime);

        return ResponseEntity.ok().body(localDate.toString() + localDateTime.toString());
    }
}
