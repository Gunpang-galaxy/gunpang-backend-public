package com.galaxy.gunpang.dailyRecord.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SleepRecordApiReqDto {

    private String sleepAt;
    private String awakeAt;
    private String recordDate;
}
