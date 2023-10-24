package com.galaxy.gunpang.exercise.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExerciseIntensity {
    HIGH("고"), MEDIUM("중"), LOW("저"), NOT("운동이 아님");

    private final String value;

}
