package com.galaxy.gunpang.dailyRecord.model.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SleepRecordReqDto {

    @NonNull
    private int sleepStartHour;

    @NonNull
    private int sleepStartMinute;

    @NonNull
    private int sleepEndHour;

    @NonNull
    private int sleepEndMinute;

}
