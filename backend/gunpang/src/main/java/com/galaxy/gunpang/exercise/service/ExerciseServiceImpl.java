package com.galaxy.gunpang.exercise.service;

import com.galaxy.gunpang.dailyRecord.exception.DailyRecordNotFoundException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import com.galaxy.gunpang.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService{

    private final ExerciseRepository exerciseRepository;
    private final DailyRecordRepository dailyRecordRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);
    @Override
    public void recordExercise(Long userId, LocalDateTime startedTime, LocalDateTime finishedTime, ExerciseIntensity exerciseIntensity) throws Exception {
        //정보들을 db에 넣어야함
        LocalDate today = LocalDate.now();
       try{
           //운동 강도, 시작 시간, 종료 시간 등록
           //만약 오늘 날짜로 하루 기록이 있다면 있는 것과 연결
           DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, today).orElseThrow(
                   () -> new DailyRecordNotFoundException(today)
           );
            logger.debug(dailyRecord.toString());
            //운동 시간 계산
            long exerciseAccTime = Duration.between(startedTime,finishedTime).toSeconds();

            dailyRecord.setExerciseAccTime(exerciseAccTime);
            //하루 기록 업데이트
            dailyRecordRepository.save(dailyRecord);
            //운동 저장
            Exercise exercise = new Exercise(dailyRecord.getId(),exerciseIntensity,startedTime,finishedTime);
            logger.debug(exercise.toString());
            exerciseRepository.save(exercise);

       }catch (DailyRecordNotFoundException e){
           throw new DailyRecordNotFoundException(today);
        } catch (Exception e){
           e.getMessage();
       }
    }

}
