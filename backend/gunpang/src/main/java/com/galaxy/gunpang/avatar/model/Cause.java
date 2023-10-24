package com.galaxy.gunpang.avatar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Cause {
    EXERCISE_LACK("운동 부족"),
    FOOD_LACK("식사 안함"),
    SLEEP_BROKEN("수면 패턴 망가짐");

    private final String description;
}
