package com.galaxy.gunpang.dailyRecord.model.dto;

import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDailyRecordOnCalendarResDto {

    private FoodType breakfastFoodType;
    private FoodType lunchFoodType;
    private FoodType dinnerFoodType;
    private String exerciseTime;
    private String sleepAt;
    private String awakeAt;
    private List<ExerciseOnDate> exercisesOnDate;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExerciseOnDate{
        @NonNull
        private String exerciseAccTime;

        @NonNull
        private ExerciseIntensity exerciseIntensity;
    }
}
