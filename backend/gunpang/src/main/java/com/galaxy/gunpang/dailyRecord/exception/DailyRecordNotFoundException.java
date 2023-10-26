package com.galaxy.gunpang.dailyRecord.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

import java.time.LocalDate;
public class DailyRecordNotFoundException extends NotFoundException {
    public DailyRecordNotFoundException(LocalDate today) {
        super("오늘의 기록이 존재하지 않습니다 : "+today);
    }
}
