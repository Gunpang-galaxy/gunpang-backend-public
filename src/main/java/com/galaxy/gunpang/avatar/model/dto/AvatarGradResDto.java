package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AvatarGradResDto implements AvatarContents{
    private int exerciseTotal;
    private int exerciseSuccessCnt;
    private int foodTotal;
    private int foodSuccessCnt;
    private int sleepTotal;
    private int sleepSuccessCnt;

    @Builder
    public AvatarGradResDto(int exerciseTotal, int exerciseSuccessCnt, int foodTotal, int foodSuccessCnt, int sleepTotal, int sleepSuccessCnt){
        this.exerciseTotal = exerciseTotal;
        this.exerciseSuccessCnt = exerciseSuccessCnt;
        this.foodTotal = foodTotal;
        this.foodSuccessCnt = foodSuccessCnt;
        this.sleepTotal = sleepTotal;
        this.sleepSuccessCnt = sleepSuccessCnt;
    }
}
