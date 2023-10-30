package com.galaxy.gunpang.dailyRecord.exception;

import com.galaxy.gunpang.common.exception.BadRequestException;


public class RecordDateNotTodayException extends BadRequestException {
    public RecordDateNotTodayException() {
        super("recordDate가 오늘이 아닙니다.");
    }
}
