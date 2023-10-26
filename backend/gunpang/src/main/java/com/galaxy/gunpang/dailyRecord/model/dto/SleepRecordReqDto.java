package com.galaxy.gunpang.dailyRecord.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SleepRecordReqDto {
    @NonNull
    private LocalDateTime sleepAt;
    @NonNull
    private LocalDateTime awakeAt;

}
