package com.galaxy.gunpang.exercise.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

import java.time.LocalDate;

public class ExerciseNotFoundException extends NotFoundException {
    public ExerciseNotFoundException(LocalDate date) {
        super("해당 날짜의 운동 기록이 존재하지 않습니다 : "+date);
    }
}
