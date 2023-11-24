package com.galaxy.gunpang.dailyRecord.model.dto;

import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDailyRecordForWatchResDto {

    private FoodType breakfastFoodType;
    private FoodType lunchFoodType;
    private FoodType dinnerFoodType;
    private String exerciseTime;
    private String sleepTime;

}
