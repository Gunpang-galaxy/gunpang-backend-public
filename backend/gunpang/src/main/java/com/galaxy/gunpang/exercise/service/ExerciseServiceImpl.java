package com.galaxy.gunpang.exercise.service;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.openFeignClient.ExerciseRecordClient;
import com.galaxy.gunpang.dailyRecord.openFeignClient.dto.ExerciseRecordResDto;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.dailyRecord.service.DailyRecordService;
import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import com.galaxy.gunpang.exercise.repository.ExerciseRepository;
import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final UserRepository userRepository;
    private final DailyRecordService dailyRecordService;
    private final ExerciseRecordClient exerciseRecordClient;

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    @Override
    public void recordExercise(Long userId, LocalDateTime startedTime, LocalDateTime finishedTime, ExerciseIntensity exerciseIntensity) throws Exception {
        //정보들을 db에 넣어야함
        LocalDate today = LocalDate.now();

        //운동 강도, 시작 시간, 종료 시간 등록
        //만약 오늘 날짜로 하루 기록이 있다면 있는 것과 연결
        DailyRecord dailyRecord = dailyRecordService.returnDailyRecordOfDate(userId, today);
        logger.debug(dailyRecord.toString());
        //운동 시간 계산
        long exerciseAccTime = Duration.between(startedTime, finishedTime).toSeconds();

        dailyRecord.setExerciseAccTime(dailyRecord.getExerciseAccTime()+exerciseAccTime);
        //하루 기록 업데이트
        dailyRecordRepository.save(dailyRecord);
        //운동 저장
        Exercise exercise = new Exercise(dailyRecord.getId(), exerciseIntensity, startedTime, finishedTime);
        logger.debug(exercise.toString());
        exerciseRepository.save(exercise);

    }

    @Override
    public void exerciseComplete(Long userId) throws Exception {
        // 사용자 정보 가져오기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );

        LocalDate date = LocalDate.now(); // 오늘 날짜
        int age = date.getYear() - user.getBirthYear(); // 사용자의 나이
        String gender = user.getGender().toString(); // 사용자의 성별

        // flask 서버에 운동 기록(심박수, 시간) 요청
        List<ExerciseRecordResDto> exerciseRecordResDto = exerciseRecordClient.getExerciseRecord(user.getGoogleId(), date.toString(), age, gender);

        // 운동 강도 계산을 위한 기준 심박수
        int maxHeartRate = 220 - age;
        int averageHeartRate = 72;
        if (gender.equals("FEMALE")) averageHeartRate = 76;
        double lowHeartRate = (maxHeartRate - averageHeartRate) * 0.65 + averageHeartRate;
        double highHeartRate = (maxHeartRate - averageHeartRate) * 0.85 + averageHeartRate;

        // 운동 강도 계산
        int low = 0, medium = 0, high = 0;
        for (int i = 0; i < exerciseRecordResDto.size(); i++) {
            double heartRate = exerciseRecordResDto.get(i).getHeartbeat();
            if (heartRate < lowHeartRate) low++;
            else if (heartRate < highHeartRate) medium++;
            else high++;
        }

        long dailyRecordId = saveTotalExerciseTime(userId, date, low + medium + high);
        saveExerciseIntensityTime(dailyRecordId, userId, low, medium, high);
    }

    // 오늘의 총 운동 시간 저장
    private Long saveTotalExerciseTime(long userId, LocalDate date, int total) {
        DailyRecord dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(userId, date).orElse(
                DailyRecord.builder()
                        .userId(userId)
                        .build()
        );

        dailyRecord.setExerciseAccTime(dailyRecord.getExerciseAccTime() + total);
        return dailyRecordRepository.save(dailyRecord).getId();
    }

    // 오늘의 운동 강도 저장
    private void saveExerciseIntensityTime(long dailyRecordId, long userId, int low, int medium, int high) {
        Exercise lowIntensity = getPrevExercise(dailyRecordId, ExerciseIntensity.LOW);
        Exercise mediumIntensity = getPrevExercise(dailyRecordId, ExerciseIntensity.MEDIUM);
        Exercise highIntensity = getPrevExercise(dailyRecordId, ExerciseIntensity.HIGH);

        // 현재 운동 강도 별 운동 진행 시간
        lowIntensity.setHeartRate(lowIntensity.getHeartRate() + low);
        mediumIntensity.setHeartRate(mediumIntensity.getHeartRate() + medium);
        highIntensity.setHeartRate(highIntensity.getHeartRate() + high);

        // 운동 강도 별 진행 시간 저장
        exerciseRepository.save(lowIntensity);
        exerciseRepository.save(mediumIntensity);
        exerciseRepository.save(highIntensity);
    }

    private Exercise getPrevExercise(long dailyRecordId, ExerciseIntensity exerciseIntensity) {
        return exerciseRepository.findAllByDailyRecordIdAndExerciseIntensity(dailyRecordId, exerciseIntensity).orElse(
                Exercise.builder()
                        .dailyRecordId(dailyRecordId)
                        .exerciseIntensity(exerciseIntensity)
                        .heartRate(0)
                        .startedTime(LocalDateTime.now())
                        .finishedTime(LocalDateTime.now())
                        .build()
        );
    }

}
