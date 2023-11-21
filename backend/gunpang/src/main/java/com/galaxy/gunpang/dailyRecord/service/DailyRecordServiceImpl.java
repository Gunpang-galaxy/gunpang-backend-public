package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.exception.RecordDateNotTodayException;
import com.galaxy.gunpang.dailyRecord.exception.FoodRecordTimeBadRequestException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordForWatchResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordOnCalendarResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordApiReqDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.openFeignClient.ExerciseRecordClient;
import com.galaxy.gunpang.dailyRecord.openFeignClient.dto.ExerciseRecordResDto;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import com.galaxy.gunpang.exercise.repository.ExerciseRepository;
import java.time.LocalTime;

import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.galaxy.gunpang.dailyRecord.model.enums.FoodType.NOT_RECORD;

@RequiredArgsConstructor
@Service
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private final ExerciseRepository exerciseRepository;

    private static final Logger logger = LoggerFactory.getLogger(DailyRecordService.class);

    public String convertExerciseAccTimeSecondToString(DailyRecord dailyRecord) {
        Duration duration = Duration.ofSeconds(dailyRecord.getExerciseAccTime());

        return String.format("%d분 %d초",
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    public FoodType processNullOnFoodType(FoodType foodType) {
        if (foodType == null) {
            foodType = NOT_RECORD;
        }
        return foodType;
    }

    public String processNullOnSleep(LocalDateTime sleepTime) {
        String sleepTimeToString = "-1";
        if (sleepTime != null) {
            logger.debug("sleepAt : " + sleepTimeToString);
            //여기가 문제
            sleepTimeToString = String.format("%02d:%02d", sleepTime.getHour(), sleepTime.getMinute());
            logger.debug("sleepAt : " + sleepTimeToString);
        }
        return sleepTimeToString;
    }

    public String processNullOnSleepForWatch(LocalDateTime sleepAt, LocalDateTime awakeAt) {
        String sleepAtToString = "00시간 00분";
        if (sleepAt != null && awakeAt != null) {
            logger.debug("sleepAtToString : " + sleepAtToString);

            Duration sleepDuration = Duration.between(sleepAt, awakeAt);

            sleepAtToString = String.format("%02d시간 %02d분",
                    sleepDuration.toHours(),
                    sleepDuration.toMinutesPart());

            logger.debug("sleepAtToString : " + sleepAtToString);

        }
        return sleepAtToString;
    }

    public DailyRecord returnDailyRecordOfDate(Long userId, LocalDate date) {

        Optional<DailyRecord> dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, date);

        if (dailyRecord.isPresent()) {
            return dailyRecord.get();
        } else {
            createRecord(userId);
            Optional<DailyRecord> createdDailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, date);
            return createdDailyRecord.get();
        }
    }

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
    public void recordSleep(Long userId, SleepRecordReqDto sleepRecordReqDto) {
        // 1. 오늘 날짜에 맞는 하루 기록 가져오기
        LocalDate today = LocalDate.now();
        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, today);
        logger.debug(dailyRecord.toString());

        // 2. 입력값 LocalTime으로 수정
        LocalTime sleepStartTime = LocalTime.of(sleepRecordReqDto.getSleepStartHour(), sleepRecordReqDto.getSleepStartMinute());
        LocalTime sleepEndTime = LocalTime.of(sleepRecordReqDto.getSleepEndHour(), sleepRecordReqDto.getSleepEndMinute());
        logger.debug("기상 시간 : {}, 수면 시간 : {}", sleepStartTime, sleepEndTime);

        // 3. LocalDateTime 형식으로 수면 시간 수정
        LocalDateTime sleepStartAt = LocalDateTime.now();
        sleepStartAt = sleepStartAt.with(sleepStartTime).with(today);
        LocalDateTime sleepEndAt = LocalDateTime.now();
        sleepEndAt = sleepEndAt.with(sleepEndTime).with(today);

        // 어제 자고 오늘 일어난 경우
        if (sleepStartAt.isAfter(sleepEndAt)) {
            sleepStartAt = sleepStartAt.minusDays(1);
        }
        logger.debug("기상 시간 : {}, 수면 시간 : {}", sleepStartAt, sleepEndAt);

        // 4. 하루 기록 수면 시각, 기상 시각 업데이트
        dailyRecord.setSleepRecord(sleepStartAt, sleepEndAt);
        logger.debug(dailyRecord.toString());
        dailyRecordRepository.save(dailyRecord);

    }

    @Override
    public void recordFood(Long userId, FoodType foodType) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //언제 밥먹었은지보고 하루 기록에 기록 + 저장

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime now = LocalDateTime.of(2023, 11, 1, 23, 30);

        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, today);
        logger.debug(dailyRecord.toString());

        if (5 <= now.getHour() && now.getHour() <= 10) {
            //이미 기록이 있을 시에는 기록 덮어쓰기
            dailyRecord.setBreakfastFoodType(foodType);
        }
        if (11 <= now.getHour() && now.getHour() <= 16) {
            dailyRecord.setLunchFoodType(foodType);
        }
        if (17 <= now.getHour() && now.getHour() <= 21) {
            dailyRecord.setDinnerFoodType(foodType);
        }
        if (22 <= now.getHour() || now.getHour() <= 4) {
            throw new FoodRecordTimeBadRequestException(now.getHour());
        }

        logger.debug(dailyRecord.toString());
        dailyRecordRepository.save(dailyRecord);

    }

    @Override
    public CheckDailyRecordResDto checkDailyRecord(Long userId, String date) {

        LocalDate localDate = LocalDate.parse(date);
        CheckDailyRecordResDto checkDailyRecordResDto;

        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, localDate);
        logger.debug(dailyRecord.toString());

        String exerciseAccTime = convertExerciseAccTimeSecondToString(dailyRecord);

        logger.debug("exerciseAccTime : " + exerciseAccTime);

        //foodType null 처리
        FoodType breakFastFoodType = processNullOnFoodType(dailyRecord.getBreakfastFoodType());
        FoodType lunchFoodType = processNullOnFoodType(dailyRecord.getLunchFoodType());
        FoodType dinnerFoodType = processNullOnFoodType(dailyRecord.getDinnerFoodType());

        //sleep null 처리
        String sleepAt = processNullOnSleep(dailyRecord.getSleepAt());
        String awakeAt = processNullOnSleep(dailyRecord.getAwakeAt());

        checkDailyRecordResDto = CheckDailyRecordResDto.builder()
                .breakfastFoodType(breakFastFoodType)
                .lunchFoodType(lunchFoodType)
                .dinnerFoodType(dinnerFoodType)
                .exerciseTime(exerciseAccTime)
                .sleepAt(sleepAt)
                .awakeAt(awakeAt)
                .build();

        return checkDailyRecordResDto;
    }

    public CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendar(Long userId, String date) {

        //해당 날짜에 맞는 기록 가져와보기
        //없으면 오류, 있으면 그 기록에서 정보 담아서 전달
        //변환해줘야 가능
        LocalDate localDate = LocalDate.parse(date);

        // 해당 날짜의 운동 기록 가져오기
        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, localDate);

        // 해당 날짜의 운동 강도 별 운동 시간 가져오기
        List<Exercise> exercises = exerciseRepository.getDailyExerciseRecordOnDateByRecordId(dailyRecord.getId()).orElse(
                List.of()
        );
        logger.debug(exercises.toString());
        List<CheckDailyRecordOnCalendarResDto.ExerciseOnDate> exerciseOnDates = new ArrayList<>();

        // 운동 강도 별 운동 시간 계산
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getHeartRate() == 0) continue; // 운동 시간이 0인 경우 제외
            String exerciseTime = String.format("%d분 %d초", exercises.get(i).getHeartRate() / 60, exercises.get(i).getHeartRate() % 60);
            exerciseOnDates.add(CheckDailyRecordOnCalendarResDto.ExerciseOnDate.builder()
                    .exerciseAccTime(exerciseTime)
                    .exerciseIntensity(exercises.get(i).getExerciseIntensity())
                    .build());
        }

        // 총 운동 시간
        String exerciseTime = convertExerciseAccTimeSecondToString(dailyRecord);

        //foodType null 처리
        FoodType breakFastFoodType = processNullOnFoodType(dailyRecord.getBreakfastFoodType());
        FoodType lunchFoodType = processNullOnFoodType(dailyRecord.getLunchFoodType());
        FoodType dinnerFoodType = processNullOnFoodType(dailyRecord.getDinnerFoodType());

        //sleep null 처리
        String sleepAt = processNullOnSleep(dailyRecord.getSleepAt());
        String awakeAt = processNullOnSleep(dailyRecord.getAwakeAt());

        logger.debug(dailyRecord.toString());
        CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendarResDto = CheckDailyRecordOnCalendarResDto.builder()
                .breakfastFoodType(breakFastFoodType)
                .lunchFoodType(lunchFoodType)
                .dinnerFoodType(dinnerFoodType)
                .exerciseTime(exerciseTime)
                .sleepAt(sleepAt)
                .awakeAt(awakeAt)
                .exercisesOnDate(exerciseOnDates)
                .build();

        return checkDailyRecordOnCalendarResDto;

    }

    @Override
    public void recordSleepWithHealthConnectApi(long userId, SleepRecordApiReqDto sleepRecordApiReqDto) {
        LocalDate localDate = LocalDate.parse(sleepRecordApiReqDto.getRecordDate());

        if (!localDate.equals(LocalDate.now())) {
            throw new RecordDateNotTodayException();
        }

        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, localDate);

        logger.debug(dailyRecord.toString());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        dailyRecord.setSleepRecord(LocalDateTime.parse(sleepRecordApiReqDto.getSleepAt(), formatter), LocalDateTime.parse(sleepRecordApiReqDto.getAwakeAt(), formatter));

        logger.debug(dailyRecord.toString());

        dailyRecordRepository.save(dailyRecord);
    }

    @Override
    public CheckDailyRecordForWatchResDto checkDailyRecordForWatch(Long userId, String date) {
        LocalDate localDate = LocalDate.parse(date);

        DailyRecord dailyRecord = returnDailyRecordOfDate(userId, localDate);

        String exerciseAccTime = convertExerciseAccTimeSecondToString(dailyRecord);

        logger.debug("exerciseAccTime : " + exerciseAccTime);

        String sleepTime = processNullOnSleepForWatch(dailyRecord.getSleepAt(), dailyRecord.getAwakeAt());

        //foodType null 처리
        FoodType breakFastFoodType = processNullOnFoodType(dailyRecord.getBreakfastFoodType());
        FoodType lunchFoodType = processNullOnFoodType(dailyRecord.getLunchFoodType());
        FoodType dinnerFoodType = processNullOnFoodType(dailyRecord.getDinnerFoodType());


        CheckDailyRecordForWatchResDto checkDailyRecordForWatchResDto = CheckDailyRecordForWatchResDto.builder()
                .breakfastFoodType(breakFastFoodType)
                .lunchFoodType(lunchFoodType)
                .dinnerFoodType(dinnerFoodType)
                .exerciseTime(exerciseAccTime)
                .sleepTime(sleepTime)
                .build();

        return checkDailyRecordForWatchResDto;
    }
}
