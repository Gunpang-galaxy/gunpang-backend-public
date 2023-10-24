package com.galaxy.gunpang.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExerciseEnum {
    HIGH("고"), MEDIUM("중"), LOW("저"), NOT("운동이 아님");

    private final String value;

}
