package com.galaxy.gunpang.goal.model;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Goal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Avatar avatar;
    @Column(nullable = false)
    private byte exerciseDay;
    @Column(nullable = false)
    private int exerciseTime;
    @Column(nullable = false)
    private LocalTime sleepStartTime;
    @Column(nullable = false)
    private LocalTime sleepEndTime;

    @Builder
    public Goal(Long id, Avatar avatar, byte exerciseDay, int exerciseTime, LocalTime sleepStartTime, LocalTime sleepEndTime){
        this.id = id;
        this.avatar = avatar;
        this.exerciseDay = exerciseDay;
        this.exerciseTime = exerciseTime;
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
    }
}
