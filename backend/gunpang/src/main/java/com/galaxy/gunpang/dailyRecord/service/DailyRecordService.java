package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordForWatchResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordOnCalendarResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordApiReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.model.enums.TimeToEat;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyRecordService {

    void createRecord(Long userId);
    public void recordSleep(Long userId, LocalDateTime sleepAt, LocalDateTime awakeAt);

    void recordFood(Long userId, FoodType foodType);

    CheckDailyRecordResDto checkDailyRecord(Long userId, String date);

    CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendar(Long userId, String date);

    void recordSleepWithHealthConnectApi(long userId, SleepRecordApiReqDto sleepRecordApiReqDto);

    CheckDailyRecordForWatchResDto checkDailyRecordForWatch(Long userId, String date);
}
