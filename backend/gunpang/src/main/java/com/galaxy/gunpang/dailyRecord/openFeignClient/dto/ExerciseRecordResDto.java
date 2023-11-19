package com.galaxy.gunpang.dailyRecord.openFeignClient.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ExerciseRecordResDto {
    private Timestamp createdAt;
    private Double heartbeat;
    private String playerId;

    public ExerciseRecordResDto(Timestamp createdAt, Double heartbeat, String playerId) {
        this.createdAt = createdAt;
        this.heartbeat = heartbeat;
        this.playerId = playerId;
    }
}
