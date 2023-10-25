package com.galaxy.gunpang.exercise.service;

import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;

import java.time.LocalDateTime;

public interface ExerciseService {

    public void recordExercise(LocalDateTime startedTime, LocalDateTime finishedTime, ExerciseIntensity exerciseIntensity) throws Exception;
}
