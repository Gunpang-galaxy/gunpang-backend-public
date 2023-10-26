package com.galaxy.gunpang.dailyRecord.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeToEat {

   BREAKFAST("아침"), LUNCH("점심"), DINNER("저녁");

    private final String value;

}
