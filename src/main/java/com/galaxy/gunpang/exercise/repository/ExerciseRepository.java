package com.galaxy.gunpang.exercise.repository;

import com.galaxy.gunpang.exercise.model.Exercise;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {
    Optional<Exercise> findAllByDailyRecordIdAndExerciseIntensity(Long dailyRecordId, ExerciseIntensity exerciseIntensity);
}
