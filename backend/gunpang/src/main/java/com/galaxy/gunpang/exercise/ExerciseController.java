package com.galaxy.gunpang.exercise;


import com.galaxy.gunpang.exercise.model.dto.ExerciseRecordReqDto;
import com.galaxy.gunpang.exercise.service.ExerciseService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/records/exercise")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;

    @Operation(summary = "오늘 운동 기록", description = "오늘 운동한 기록을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "운동 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "운동 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> recordExercise(@RequestHeader("Authorization") String token, @RequestBody ExerciseRecordReqDto exerciseRecordReqDto) throws Exception {
        Long userId = userService.getIdByToken(token).getId();
        exerciseService.recordExercise(userId,exerciseRecordReqDto.getStartedTime(),exerciseRecordReqDto.getFinishedTime(),exerciseRecordReqDto.getExerciseIntensity());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
