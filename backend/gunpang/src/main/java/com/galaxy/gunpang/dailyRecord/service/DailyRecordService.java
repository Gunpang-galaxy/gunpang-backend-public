package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.model.enums.TimeToEat;

import java.time.LocalDateTime;

public interface DailyRecordService {
    void recordSleep(Long userId, LocalDateTime sleepAt, LocalDateTime awakeAt);

    void recordFood(Long userId, FoodType foodType, TimeToEat timeToEat);
}
