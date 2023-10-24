package com.galaxy.gunpang.dailyRecord.entity;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
    private FoodEnum breakfastFoodType;

    @Column()
    @Enumerated(EnumType.STRING)
    private FoodEnum lunchFoodType;

    @Column()
    @Enumerated(EnumType.STRING)
    private FoodEnum dinnerFoodType;

    @Column()
    private LocalDateTime sleepAt;

    @Column()
    private LocalDateTime awakeAt;


}
