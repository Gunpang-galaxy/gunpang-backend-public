package com.galaxy.gunpang.goal.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Goal extends BaseEntity {
    @Id
    private Long id;
    private Long avatarId;
    @Column(columnDefinition = "TINYINT", nullable = false)
    private int exerciseDay;
    @Column(nullable = false)
    private int exerciseTime;
    @Column(nullable = false)
    private LocalTime sleepStartTime;
    @Column(nullable = false)
    private LocalTime sleepEndTime;

    @Builder
    public Goal(Long id, Long avatarId, int exerciseDay, int exerciseTime, LocalTime sleepStartTime, LocalTime sleepEndTime){
        this.id = id;
        this.avatarId = avatarId;
        this.exerciseDay = exerciseDay;
        this.exerciseTime = exerciseTime;
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
    }
}
