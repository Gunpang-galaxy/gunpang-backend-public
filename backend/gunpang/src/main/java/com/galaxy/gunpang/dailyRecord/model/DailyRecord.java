package com.galaxy.gunpang.dailyRecord.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@DynamicInsert
@Table(name = "DAILY_RECORD")
public class DailyRecord extends BaseEntity {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate recordDate;

    @Column()
    @ColumnDefault("0")
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

    public void setBreakfastFoodType(FoodType breakfastFoodType) {
        this.breakfastFoodType = breakfastFoodType;
    }

    public void setLunchFoodType(FoodType lunchFoodType) {
        this.lunchFoodType = lunchFoodType;
    }

    public void setDinnerFoodType(FoodType dinnerFoodType) {
        this.dinnerFoodType = dinnerFoodType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
