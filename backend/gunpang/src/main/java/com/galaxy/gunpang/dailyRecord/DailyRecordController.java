package com.galaxy.gunpang.dailyRecord;

import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수면 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "수면 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/records/sleep", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> recordSleep(@RequestHeader("Authorization") String token, @RequestBody SleepRecordReqDto sleepRecordReqDto) throws Exception {

        dailyRecordService.recordSleep(sleepRecordReqDto.getSleepAt(),sleepRecordReqDto.getAwakeAt());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
