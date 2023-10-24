package com.galaxy.gunpang.exercise.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "EXERCISE")
public class Exercise extends BaseEntity {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private Long dailyRecordId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseIntensity exerciseIntensity;

    @Column(nullable = false)
    private LocalDateTime startedTime;

    @Column(nullable = false)
    private LocalDateTime finishedTime;

    @Column(nullable = false)
    private int heartRate;

}
