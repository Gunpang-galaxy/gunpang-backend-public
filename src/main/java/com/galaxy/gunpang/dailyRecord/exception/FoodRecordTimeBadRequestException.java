package com.galaxy.gunpang.dailyRecord.exception;

import com.galaxy.gunpang.common.exception.BadRequestException;

public class FoodRecordTimeBadRequestException extends BadRequestException {

    public FoodRecordTimeBadRequestException(int hour) {
        super("지금은 식사를 기록할 수 있는 시간이 아닙니다."+ hour);
    }
}
