package com.galaxy.gunpang.dailyRecord.repository;


import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.exercise.model.Exercise;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecordRepositoryCustom {

    public Optional<DailyRecord> getDailyRecordOnTodayByUserId(Long userId, LocalDate today);


}
