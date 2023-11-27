package com.galaxy.gunpang.dailyRecord.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodType {
    HEALTHY("건강"), NORMAL("적당"), BAD("불량"),NOT_RECORD("기록 없음");

    private final String value;

}

