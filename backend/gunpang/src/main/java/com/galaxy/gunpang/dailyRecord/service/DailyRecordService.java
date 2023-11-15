package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordForWatchResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordOnCalendarResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordApiReqDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;

import java.time.LocalDate;

public interface DailyRecordService {

    void createRecord(Long userId);
    void recordSleep(Long userId, SleepRecordReqDto sleepRecordReqDto);

    void recordFood(Long userId, FoodType foodType);

    CheckDailyRecordResDto checkDailyRecord(Long userId, String date);

    CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendar(Long userId, String date);

    void recordSleepWithHealthConnectApi(long userId, SleepRecordApiReqDto sleepRecordApiReqDto);

    CheckDailyRecordForWatchResDto checkDailyRecordForWatch(Long userId, String date);

    DailyRecord returnDailyRecordOfDate(Long userId, LocalDate today);
}
