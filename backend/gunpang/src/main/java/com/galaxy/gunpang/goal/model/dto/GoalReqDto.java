package com.galaxy.gunpang.goal.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class GoalReqDto {
    @NonNull
    private Long avatarId;
    @NonNull
    private int startTime;
    @NonNull
    private int startMinute;
    @NonNull
    private int entTime;
    @NonNull
    private int endMinute;
    @NonNull
    private int exerciseDay;
    @NonNull
    private int exerciseTime;
}
