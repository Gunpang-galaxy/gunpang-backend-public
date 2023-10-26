package com.galaxy.gunpang.dailyRecord.repository;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long>, DailyRecordRepositoryCustom {


}

