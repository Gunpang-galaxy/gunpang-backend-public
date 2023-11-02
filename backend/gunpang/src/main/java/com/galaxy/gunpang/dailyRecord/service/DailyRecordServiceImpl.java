package com.galaxy.gunpang.dailyRecord.service;

import com.galaxy.gunpang.dailyRecord.exception.DailyRecordNotFoundException;
import com.galaxy.gunpang.dailyRecord.exception.RecordDateNotTodayException;
import com.galaxy.gunpang.dailyRecord.exception.FoodRecordTimeBadRequestException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordForWatchResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordOnCalendarResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.CheckDailyRecordResDto;
import com.galaxy.gunpang.dailyRecord.model.dto.SleepRecordApiReqDto;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
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

import static com.galaxy.gunpang.dailyRecord.model.enums.FoodType.NOT_RECORD;

@RequiredArgsConstructor
@Service
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private final ExerciseRepository exerciseRepository;

    private static final Logger logger = LoggerFactory.getLogger(DailyRecordService.class);

    public String convertExerciseAccTimeSecondToString(DailyRecord dailyRecord) {
        Duration duration = Duration.ofSeconds(dailyRecord.getExerciseAccTime());

        return String.format("%02d시 %02d분",
                duration.toHours(),
                duration.toMinutesPart());
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
    public void recordFood(Long userId, FoodType foodType) {
        //오늘 날짜에 맞는 하루 기록 가져오기
        //언제 밥먹었은지보고 하루 기록에 기록 + 저장

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime now = LocalDateTime.of(2023, 11, 1, 23, 30);

        DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, today).orElseThrow(
                () -> new DailyRecordNotFoundException(today)
        );
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
            Duration duration = Duration.between(exercises.get(i).getStartedTime(), exercises.get(i).getFinishedTime());
            String exerciseAccTime = String.format("%02d:%02d:%02d",
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart());

            exerciseOnDates.add(CheckDailyRecordOnCalendarResDto.ExerciseOnDate.builder()
                    .exerciseAccTime(exerciseAccTime)
                    .exerciseIntensity(exercises.get(i).getExerciseIntensity())
                    .build());
        }
        Duration duration = Duration.ofSeconds(dailyRecord.getExerciseAccTime());
        String formattedTime = String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());

        logger.debug(dailyRecord.toString());
        CheckDailyRecordOnCalendarResDto checkDailyRecordOnCalendarResDto = CheckDailyRecordOnCalendarResDto.builder()
                .breakfastFoodType(dailyRecord.getBreakfastFoodType())
                .lunchFoodType(dailyRecord.getLunchFoodType())
                .dinnerFoodType(dailyRecord.getDinnerFoodType())
                .exerciseTime(formattedTime)
                .sleepAt(dailyRecord.getSleepAt().toString())
                .awakeAt(dailyRecord.getAwakeAt().toString())
                .exercisesOnDate(exerciseOnDates)
                .build();

        return checkDailyRecordOnCalendarResDto;

    }

    @Override
    public void recordSleepWithHealthConnectApi(long userId, SleepRecordApiReqDto sleepRecordApiReqDto) {
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

    @Override
    public CheckDailyRecordForWatchResDto checkDailyRecordForWatch(Long userId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        CheckDailyRecordForWatchResDto checkDailyRecordForWatchResDto = null;
        DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, localDate).orElseThrow(
                () -> new DailyRecordNotFoundException(localDate)
        );
        logger.debug(dailyRecord.toString());

        Duration exerciseDuration = Duration.ofSeconds(dailyRecord.getExerciseAccTime());

        String exerciseFormattedTime = String.format("%02d시 %02d분",
                exerciseDuration.toHours(),
                exerciseDuration.toMinutesPart());

        Duration sleepDuration = Duration.between(dailyRecord.getSleepAt(), dailyRecord.getAwakeAt());

        String sleepFormattedTime = String.format("%02d시 %02d분",
                sleepDuration.toHours(),
                sleepDuration.toMinutesPart());

        checkDailyRecordForWatchResDto = CheckDailyRecordForWatchResDto.builder()
                .breakfastFoodType(dailyRecord.getBreakfastFoodType())
                .lunchFoodType(dailyRecord.getLunchFoodType())
                .dinnerFoodType(dailyRecord.getDinnerFoodType())
                .exerciseTime(exerciseFormattedTime)
                .sleepTime(sleepFormattedTime)
                .build();

        return checkDailyRecordForWatchResDto;
    }
}
