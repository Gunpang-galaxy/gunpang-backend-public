package com.galaxy.gunpang.exercise.repository;

import com.galaxy.gunpang.exercise.model.Exercise;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.galaxy.gunpang.exercise.model.QExercise.exercise;

@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Exercise>> getDailyExerciseRecordOnDateByRecordId(Long dailyRecordId) {
        return Optional.ofNullable(queryFactory.selectFrom(exercise)
                .where(exercise.dailyRecordId.eq(dailyRecordId))
                .fetch());
    }
}
