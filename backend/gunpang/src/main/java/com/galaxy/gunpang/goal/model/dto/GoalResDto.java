package com.galaxy.gunpang.goal.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
public class GoalResDto {
    private String sleepStart;
    private String sleepEnd;
    private int exerciseDay;
    private String exerciseTime;

    @Builder
    public GoalResDto(String sleepStart, String sleepEnd, int exerciseDay, int exerciseTime){
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.exerciseDay = exerciseDay;
        Duration duration = Duration.ofMinutes(exerciseTime);
        this.exerciseTime = String.format("%02d시간 %02d분", duration.toHours(), duration.toMinutesPart());
    }
}
