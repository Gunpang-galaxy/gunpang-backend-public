package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DailyRecordServiceImpl implements DailyRecordService{

    private final DailyRecordRepository dailyRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);
    @Override
    public void recordSleep(LocalDateTime sleepAt, LocalDateTime awakeAt) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //하루 기록 수면 시각, 기상 시각 업데이트
        try{
            LocalDate today = LocalDate.now();
            DailyRecord dailyRecord = dailyRecordRepository.findByRecordDate(today);
            logger.debug(dailyRecord.toString());
            dailyRecord.setSleepAt(sleepAt);
            dailyRecord.setAwakeAt(awakeAt);
            logger.debug(dailyRecord.toString());
            dailyRecordRepository.save(dailyRecord);
        }catch(Exception e){
            e.getMessage();
        }

    }
}
