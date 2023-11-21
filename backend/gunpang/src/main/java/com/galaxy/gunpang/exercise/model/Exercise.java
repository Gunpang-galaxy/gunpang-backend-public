package com.galaxy.gunpang.exercise.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.exercise.model.enums.ExerciseIntensity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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

    @Column()
    private Integer heartRate;

    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    @Builder
    public Exercise(Long dailyRecordId, ExerciseIntensity exerciseIntensity, LocalDateTime startedTime, LocalDateTime finishedTime) {
        this.dailyRecordId = dailyRecordId;
        this.exerciseIntensity = exerciseIntensity;
        this.startedTime = startedTime;
        this.finishedTime = finishedTime;
    }

}
