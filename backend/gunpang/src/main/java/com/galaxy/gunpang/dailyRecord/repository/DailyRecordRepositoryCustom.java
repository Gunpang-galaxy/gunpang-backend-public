package com.galaxy.gunpang.dailyRecord.repository;


import com.galaxy.gunpang.dailyRecord.model.DailyRecord;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecordRepositoryCustom {

    public Optional<DailyRecord> getDailyRecordOnTodayByUserId(Long userId, LocalDate today);
    //DailyRecord findByRecordDate(LocalDate today);

    //DailyRecord findByUserId(Long userId);

}
