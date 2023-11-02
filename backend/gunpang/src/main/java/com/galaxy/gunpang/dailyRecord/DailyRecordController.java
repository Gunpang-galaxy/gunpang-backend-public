package com.galaxy.gunpang.dailyRecord;

import com.galaxy.gunpang.dailyRecord.model.dto.*;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import com.galaxy.gunpang.exercise.service.ExerciseService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(DailyRecordController.class);

    @Operation(summary = "하루 기록 생성", description = "하루 기록을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "하루 기록 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "하루 기록 생성 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/createTodayRecord")
    public ResponseEntity<?> createRecord(@RequestHeader("Authorization") String token) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        dailyRecordService.createRecord(userId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "오늘 수면 기록", description = "수동으로 저장하는 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수면 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "수면 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/sleep")
    public ResponseEntity<?> recordSleep(@RequestHeader("Authorization") String token, @RequestBody SleepRecordReqDto sleepRecordReqDto) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        dailyRecordService.recordSleep(userId,sleepRecordReqDto.getSleepAt(),sleepRecordReqDto.getAwakeAt());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "오늘 수면 기록", description = "삼성 헬스에서 받아서 저장하는 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "삼성 헬스 수면 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 기록 날짜와 오늘이 다름"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "삼성 헬스 수면 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/sleep/samsung")
    public ResponseEntity<?> recordSleepWithHealthConnectApi(@RequestHeader("Authorization") String token, @RequestBody SleepRecordApiReqDto sleepRecordApiReqDto) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        dailyRecordService.recordSleepWithHealthConnectApi(userId,sleepRecordApiReqDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "오늘 식사 기록", description = "오늘 식사한 기록을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식사 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "식사 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/food")
    public ResponseEntity<?> recordFood(@RequestHeader("Authorization") String token, @RequestBody FoodType foodType) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        dailyRecordService.recordFood(userId, foodType);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "하루 기록 조회", description = "메인 화면용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하루 기록 가져오기 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "하루 기록이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping(value = "/records")
    public ResponseEntity<?> checkDailyRecord(@RequestHeader("Authorization") String token, @RequestParam String date ){
        Long userId = userService.getIdByToken(token).getId();
        CheckDailyRecordResDto checkDailyRecordResDto = dailyRecordService.checkDailyRecord(userId, date);

        return new ResponseEntity<>(checkDailyRecordResDto, HttpStatus.OK);
    }

    @Operation(summary = "하루 기록 조회", description = "워치용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하루 기록 가져오기 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "하루 기록이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping(value = "/records/watch")
    public ResponseEntity<?> checkDailyRecordForWatch(@RequestHeader("Authorization") String token, @RequestParam String date ){
        Long userId = userService.getIdByToken(token).getId();
        CheckDailyRecordForWatchResDto checkDailyRecordForWatchResDto = dailyRecordService.checkDailyRecordForWatch(userId, date);

        return new ResponseEntity<>(checkDailyRecordForWatchResDto, HttpStatus.OK);
    }

    @Operation(summary = "하루 기록 조회", description = "달력용")
    @GetMapping(value = "/records/calendar")
    public ResponseEntity<?> checkDailyRecordOnCalendar(@RequestHeader("Authorization") String token, @RequestParam String date){

        Long userId = userService.getIdByToken(token).getId();

        logger.debug(String.valueOf(userId));
        CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendarResDto = dailyRecordService.checkDailyRecordOnCalendar(userId, date);
        return new ResponseEntity<>(checkDailyRecordOnCalendarResDto, HttpStatus.OK);

    }

}
