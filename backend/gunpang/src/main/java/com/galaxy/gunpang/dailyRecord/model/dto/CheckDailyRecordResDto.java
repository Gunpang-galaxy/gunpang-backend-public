package com.galaxy.gunpang.dailyRecord.model.dto;

import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDailyRecordResDto {

    private FoodType breakfastFoodType;
    private FoodType lunchFoodType;
    private FoodType dinnerFoodType;
    private Long exerciseTime;
    private LocalDateTime sleepAt;
    private LocalDateTime awakeAt;
}