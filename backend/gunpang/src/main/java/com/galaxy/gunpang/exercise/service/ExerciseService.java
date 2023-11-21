package com.galaxy.gunpang.exercise.service;

import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;

import java.time.LocalDateTime;

public interface ExerciseService {

    public void recordExercise(Long userId, LocalDateTime startedTime, LocalDateTime finishedTime, ExerciseIntensity exerciseIntensity) throws Exception;

    // 운동 완료 후 flask 서버에 운동 기록(심박수, 시간) 요청
    public void exerciseComplete(Long userId) throws Exception;

}
