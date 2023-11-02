package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarWatchResDto {
    private String avatarType;
    private Status status;
    private float healthPoint;
    private Stage stage;

    @Builder
    public AvatarWatchResDto(String avatarType, Status status, float healthPoint, Stage stage){
        this.avatarType = avatarType;
        this.status = status;
        this.healthPoint = healthPoint;
        this.stage = stage;
    }
}
