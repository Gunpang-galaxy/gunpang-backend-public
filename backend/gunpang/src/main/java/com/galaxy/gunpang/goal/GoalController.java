package com.galaxy.gunpang.goal;

import com.galaxy.gunpang.avatar.model.dto.AvatarIdReqDto;
import com.galaxy.gunpang.common.model.dto.MessageResDto;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.goal.model.dto.CalendarResDto;
import com.galaxy.gunpang.goal.model.dto.GoalReqDto;
import com.galaxy.gunpang.goal.model.dto.GoalResDto;
import com.galaxy.gunpang.goal.service.GoalService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Goal", description = "목표 API")
@Slf4j
@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    private final UserService userService;

    @Operation(summary = "아바타 목표 입력", description = "아바타의 목표를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 아바타")
            , @ApiResponse(responseCode = "409", description = "이미 목표가 존재합니다.")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/sleep")
    public ResponseEntity<?> add(@RequestHeader("Authorization") String token, @RequestBody GoalReqDto goalReqDto){
        Long userId = userService.getIdByToken(token).getId();

        log.debug("[POST] add method {}", goalReqDto);

        goalService.addGoal(userId, goalReqDto);

        return ResponseEntity.ok().body(MessageResDto.msg("목표 생성 성공"));
    }

    @Operation(summary = "아바타 목표 보기", description = "아바타의 목표를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 대상(아바타, 목표)")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping
    public ResponseEntity<?> get(@RequestBody AvatarIdReqDto avatarIdReqDto){
        log.debug("[POST] get method {}", avatarIdReqDto);

        GoalResDto goalResDto =  goalService.getGoal(avatarIdReqDto);

        return ResponseEntity.ok().body(goalResDto);
    }

    @Operation(summary = "월별 목표 달성 기록 가져오기", description = "월별 목표 달성 기록을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 대상(아바타, 목표)")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping
    public ResponseEntity<?> getCalendar(@RequestHeader("Authorization") String token, @RequestParam int year, @RequestParam int month){
        Long userId = userService.getIdByToken(token).getId();

        log.debug("[GET] getCalendar method {} {} {}", userId, year, month);

        CalendarResDto calendarResDto = goalService.getCalendar(userId, year, month);

        return ResponseEntity.ok().body(calendarResDto);
    }
}
