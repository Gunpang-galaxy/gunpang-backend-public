package com.galaxy.gunpang.dailyRecord.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodEnum {
    HEALTH("건강"), MEDIUM("적당"), UNHEALTH("불량");

    private final String value;

}
