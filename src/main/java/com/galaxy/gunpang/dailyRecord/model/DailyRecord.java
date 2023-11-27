package com.galaxy.gunpang.dailyRecord.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate recordDate = LocalDate.now();

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

    public void setRecordDate(LocalDate recordDate) {this.recordDate = recordDate;}

    public void setExerciseAccTime(Long exerciseAccTime) {
        this.exerciseAccTime = exerciseAccTime;
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

    public void setSleepRecord(LocalDateTime sleepAt, LocalDateTime awakeAt) {
        this.sleepAt = sleepAt;
        this.awakeAt = awakeAt;
    }

}
