package com.galaxy.gunpang.dailyRecord.model.dto;

import com.galaxy.gunpang.avatar.model.dto.AvatarContents;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDailyRecordResDto implements AvatarContents {

    private FoodType breakfastFoodType;
    private FoodType lunchFoodType;
    private FoodType dinnerFoodType;
    private String exerciseTime;
    private String sleepAt;
    private String awakeAt;
}
