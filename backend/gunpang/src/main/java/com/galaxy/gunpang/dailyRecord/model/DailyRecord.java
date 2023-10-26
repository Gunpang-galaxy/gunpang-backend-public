package com.galaxy.gunpang.dailyRecord.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name = "DAILY_RECORD")
public class DailyRecord extends BaseEntity {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column()
    private Long exerciseAccTime;

    @Column()
    @Enumerated(EnumType.STRING)
    private FoodType breakfastFoodType;

    @Column()
    @Enumerated(EnumType.STRING)
    private FoodType lunchFoodType;

    @Column()
    @Enumerated(EnumType.STRING)
    private FoodType dinnerFoodType;

    @Column()
    private LocalDateTime sleepAt;

    @Column()
    private LocalDateTime awakeAt;

    public void setExerciseAccTime(Long exerciseAccTime) {
        this.exerciseAccTime = exerciseAccTime;
    }

    public void setSleepAt(LocalDateTime sleepAt) {
        this.sleepAt = sleepAt;
    }

    public void setAwakeAt(LocalDateTime awakeAt) {
        this.awakeAt = awakeAt;
    }
}
