package com.galaxy.gunpang.dailyRecord.model.dto;

import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.model.enums.TimeToEat;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class FoodRecordReqDto {

    @NonNull
    private final Long userId;
    @NonNull
    private final FoodType foodType;
    @NonNull
    private final TimeToEat timeToEat;

}
