package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.common.exception.BadRequestException;
import com.galaxy.gunpang.dailyRecord.exception.DailyRecordNotFoundException;
import com.galaxy.gunpang.dailyRecord.exception.RecordDateNotTodayException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordOnCalendarResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordApiReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.model.enums.TimeToEat;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.exercise.exception.ExerciseNotFoundException;
import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private final ExerciseRepository exerciseRepository;

    private static final Logger logger = LoggerFactory.getLogger(DailyRecordService.class);

    @Override
    public void createRecord(Long userId) {
        //LocalDate today = LocalDate.now();
        try {
            DailyRecord dailyRecord = new DailyRecord();
            dailyRecord.setUserId(userId);
            logger.debug(dailyRecord.toString());
            dailyRecordRepository.save(dailyRecord);

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void recordSleep(Long userId, LocalDateTime sleepAt, LocalDateTime awakeAt) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //하루 기록 수면 시각, 기상 시각 업데이트
        LocalDate today = LocalDate.now();
        try {
            DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, today).orElseThrow(
                    () -> new DailyRecordNotFoundException(today)
            );
            logger.debug(dailyRecord.toString());
            dailyRecord.setSleepAt(sleepAt);
            dailyRecord.setAwakeAt(awakeAt);
            logger.debug(dailyRecord.toString());
            dailyRecordRepository.save(dailyRecord);

        } catch (DailyRecordNotFoundException e) {
            throw new DailyRecordNotFoundException(today);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void recordFood(Long userId, FoodType foodType, TimeToEat timeToEat) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //언제 밥먹었은지보고 하루 기록에 기록 + 저장
        //로그인한 사용자 것의 기록인지 추가해야함
        LocalDate today = LocalDate.now();
        try {
            DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, today).orElseThrow(
                    () -> new DailyRecordNotFoundException(today)
            );
            logger.debug(dailyRecord.toString());

            //이거다
            logger.debug(String.valueOf(timeToEat.getValue().equals("아침")));

            if (timeToEat.getValue().equals("아침")) {
                dailyRecord.setBreakfastFoodType(foodType);
            }
            if (timeToEat.getValue().equals("점심")) {
                dailyRecord.setLunchFoodType(foodType);
            }
            if (timeToEat.getValue().equals("저녁")) {
                dailyRecord.setDinnerFoodType(foodType);
            }

            logger.debug(dailyRecord.toString());

            dailyRecordRepository.save(dailyRecord);
        } catch (DailyRecordNotFoundException e) {
            throw new DailyRecordNotFoundException(today);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public CheckDailyRecordResDto checkDailyRecord(Long userId, String date) {
        //해당 날짜에 맞는 기록 가져와보기
        //없으면 오류, 있으면 그 기록에서 정보 담아서 전달
        //변환해줘야 가능
        LocalDate localDate = LocalDate.parse(date);
        CheckDailyRecordResDto checkDailyRecordResDto = null;
        DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, localDate).orElseThrow(
                () -> new DailyRecordNotFoundException(localDate)
        );
        logger.debug(dailyRecord.toString());

        Duration duration = Duration.ofSeconds(dailyRecord.getExerciseAccTime());

        String formattedTime = String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());


        checkDailyRecordResDto = CheckDailyRecordResDto.builder()
                .breakfastFoodType(dailyRecord.getBreakfastFoodType())
                .lunchFoodType(dailyRecord.getLunchFoodType())
                .dinnerFoodType(dailyRecord.getDinnerFoodType())
                .exerciseTime(formattedTime)
                .sleepAt(dailyRecord.getSleepAt().toString())
                .awakeAt(dailyRecord.getAwakeAt().toString())
                .build();

        return checkDailyRecordResDto;
    }

    public CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendar(Long userId, String date) {

        //해당 날짜에 맞는 기록 가져와보기
        //없으면 오류, 있으면 그 기록에서 정보 담아서 전달
        //변환해줘야 가능
        LocalDate localDate = LocalDate.parse(date);

        DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, localDate).orElseThrow(
                () -> new DailyRecordNotFoundException(localDate)
        );

        List<Exercise> exercises = exerciseRepository.getDailyExerciseRecordOnDateByRecordId(dailyRecord.getId()).orElseThrow(
                //비어있어도 에러 안남... 안나는게 맞는듯
        );
        logger.debug(exercises.toString());
        List<CheckDailyRecordOnCalendarResDto.ExerciseOnDate> exerciseOnDates = new ArrayList<>();

        //운돌들 가져온 거에서 운동 시간 계산해주고 리스트로 넣어야될듯

        for (int i = 0; i < exercises.size(); i++) {
            //운동 시간 계산
            long exerciseAccTime = Duration.between(exercises.get(i).getStartedTime(), exercises.get(i).getFinishedTime()).toSeconds();
            exerciseOnDates.add(CheckDailyRecordOnCalendarResDto.ExerciseOnDate.builder()
                    .exerciseAccTime(exerciseAccTime)
                    .exerciseIntensity(exercises.get(i).getExerciseIntensity())
                    .build());
        }
        logger.debug(dailyRecord.toString());
        CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendarResDto = CheckDailyRecordOnCalendarResDto.builder()
                .breakfastFoodType(dailyRecord.getBreakfastFoodType())
                .lunchFoodType(dailyRecord.getLunchFoodType())
                .dinnerFoodType(dailyRecord.getDinnerFoodType())
                .exerciseTime(dailyRecord.getExerciseAccTime())
                .sleepAt(dailyRecord.getSleepAt().toString())
                .awakeAt(dailyRecord.getAwakeAt().toString())
                .exercisesOnDate(exerciseOnDates)
                .build();

        return checkDailyRecordOnCalendarResDto;

    }

    @Override
    public void recordSleepWithHealthConnectApi(SleepRecordApiReqDto sleepRecordApiReqDto) {
        Long userId = sleepRecordApiReqDto.getUserId();
        LocalDate localDate = sleepRecordApiReqDto.getRecordDate();

        if (!localDate.equals(LocalDate.now())) {
            throw new RecordDateNotTodayException();
        }
        DailyRecord dailyRecord;
        try {
            dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, localDate).orElseThrow(
                    () -> new DailyRecordNotFoundException(localDate)
            );
        } catch (DailyRecordNotFoundException e) {
            dailyRecord = new DailyRecord();
            dailyRecord.setUserId(userId);
            logger.debug(dailyRecord.toString());
            dailyRecordRepository.save(dailyRecord);
        }

        dailyRecord.setSleepAt(sleepRecordApiReqDto.getSleepAt());
        dailyRecord.setAwakeAt(sleepRecordApiReqDto.getAwakeAt());

        logger.debug(dailyRecord.toString());

        dailyRecordRepository.save(dailyRecord);
    }
}
