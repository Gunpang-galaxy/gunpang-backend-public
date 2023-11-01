package com.galaxy.gunpang.goal.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalResDto {
    private String sleepStart;
    private String sleepEnd;
    private int exerciseDay;
    private int exerciseTime;
}
