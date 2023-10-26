package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.dailyRecord.exception.DailyRecordNotFoundException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.model.enums.TimeToEat;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DailyRecordServiceImpl implements DailyRecordService{

    private final DailyRecordRepository dailyRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(DailyRecordService.class);
    @Override
    public void recordSleep(LocalDateTime sleepAt, LocalDateTime awakeAt) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //하루 기록 수면 시각, 기상 시각 업데이트
//        try{
//            LocalDate today = LocalDate.now();
//            DailyRecord dailyRecord = dailyRecordRepository.findByRecordDate(today);
//            logger.debug(dailyRecord.toString());
//            dailyRecord.setSleepAt(sleepAt);
//            dailyRecord.setAwakeAt(awakeAt);
//            logger.debug(dailyRecord.toString());
//            dailyRecordRepository.save(dailyRecord);
//        }catch(Exception e){
//            e.getMessage();
//        }

    }

    @Override
    public void recordFood(Long userId, FoodType foodType, TimeToEat timeToEat) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //언제 밥먹었은지보고 하루 기록에 기록 + 저장
        //로그인한 사용자 것의 기록인지 추가해야함
        LocalDate today = LocalDate.now();
        try{
            DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, today).orElseThrow(
                    () -> new DailyRecordNotFoundException(today)
            );
            logger.debug(dailyRecord.toString());

            //이거다
            logger.debug(String.valueOf(timeToEat.getValue().equals("아침")));

            if (timeToEat.getValue().equals("아침")){
                dailyRecord.setBreakfastFoodType(foodType);
            }
            if (timeToEat.getValue().equals("점심")){
                dailyRecord.setLunchFoodType(foodType);
            }
            if (timeToEat.getValue().equals("저녁")){
                dailyRecord.setDinnerFoodType(foodType);
            }

            logger.debug(dailyRecord.toString());

            dailyRecordRepository.save(dailyRecord);
        }catch (DailyRecordNotFoundException e){
            throw new DailyRecordNotFoundException(today);
        } catch(Exception e){
            e.getMessage();
        }

    }
}
