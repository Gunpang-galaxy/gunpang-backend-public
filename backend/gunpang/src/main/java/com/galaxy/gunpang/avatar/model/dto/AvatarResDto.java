package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.goal.model.dto.GoalResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvatarResDto {
    private String avatarType;
    private String name;
    private Stage stage;
    private Status status;
    private float healthPoint;
    private String startedDate;
    private String finishedDate;
    private GoalResDto goal;
    private AvatarContents contents;
    private long prev;
    private long next;

    public void setContents(AvatarContents contents) {
        this.contents = contents;
    }

    @Builder
    public AvatarResDto(String avatarType, String name, Stage stage, Status status, float healthPoint, String startedDate, String finishedDate, GoalResDto goal, long prev, long next){
        this.avatarType = avatarType;
        this.name = name;
        this.stage = stage;
        this.status = status;
        this.healthPoint = healthPoint;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
        this.goal = goal;
        this.prev = prev;
        this.next = next;
    }
}
