package com.galaxy.gunpang.dailyRecord;

import com.galaxy.gunpang.dailyRecord.model.dto.FoodRecordReqDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import com.galaxy.gunpang.exercise.service.ExerciseService;
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
    private static final Logger logger = LoggerFactory.getLogger(DailyRecordController.class);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수면 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "수면 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/sleep", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> recordSleep(@RequestBody SleepRecordReqDto sleepRecordReqDto) throws Exception {

        dailyRecordService.recordSleep(sleepRecordReqDto.getUserId(),sleepRecordReqDto.getSleepAt(),sleepRecordReqDto.getAwakeAt());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식사 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "식사 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/food", consumes = "application/json;charset=UTF-8")
    //@RequestHeader("Authorization") String token
    public ResponseEntity<?> recordFood(@RequestBody FoodRecordReqDto foodRecordReqDto) throws Exception {
        //토큰에서 빼낸 userId도 넣어야 함

        dailyRecordService.recordFood(foodRecordReqDto.getUserId(), foodRecordReqDto.getFoodType(),foodRecordReqDto.getTimeToEat());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
