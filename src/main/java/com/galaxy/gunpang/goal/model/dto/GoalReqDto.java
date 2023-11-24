package com.galaxy.gunpang.goal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalReqDto {
    @NonNull
    private int startTime;
    @NonNull
    private int startMinute;
    @NonNull
    private int endTime;
    @NonNull
    private int endMinute;
    @NonNull
    private int exerciseDay;
    @NonNull
    private int exerciseTime;
}
