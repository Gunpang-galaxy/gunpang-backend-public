package com.galaxy.gunpang.exercise.repository;

import com.galaxy.gunpang.exercise.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {

}
