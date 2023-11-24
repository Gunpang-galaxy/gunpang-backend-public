package com.galaxy.gunpang.exercise.repository;


import com.galaxy.gunpang.exercise.model.Exercise;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepositoryCustom {

    public Optional<List<Exercise>> getDailyExerciseRecordOnDateByRecordId(Long dailyRecordId);


}
