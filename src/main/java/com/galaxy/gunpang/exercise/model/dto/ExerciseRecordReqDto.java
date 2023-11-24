package com.galaxy.gunpang.exercise.model.dto;

import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRecordReqDto {

    @NonNull
    private Long userId;

    @NonNull
    private LocalDateTime startedTime;
    @NonNull
    private LocalDateTime finishedTime;
    @NonNull
    private ExerciseIntensity exerciseIntensity;

}
