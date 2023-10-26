package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;

import java.time.LocalDateTime;

public interface DailyRecordService {
    public void recordSleep(LocalDateTime sleepAt, LocalDateTime awakeAt);
}
