package com.galaxy.gunpang.exercise;


import com.galaxy.gunpang.exercise.model.dto.ExerciseRecordReqDto;
import com.galaxy.gunpang.exercise.service.ExerciseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/record/exercise")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "운동 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "운동 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> recordExercise(@RequestHeader("Authorization") String token, @RequestBody ExerciseRecordReqDto exerciseRecordReqDto) throws Exception {

        exerciseService.recordExercise(exerciseRecordReqDto.getStartedTime(),exerciseRecordReqDto.getFinishedTime(),exerciseRecordReqDto.getExerciseIntensity());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}