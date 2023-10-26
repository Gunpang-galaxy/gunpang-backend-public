package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.AvatarType;
import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvatarAliveResDto extends AvatarResDto{
    private String stage;
    private byte healthPoint;

    @Builder
    public AvatarAliveResDto(AvatarType avatarType, String name, Stage stage, Status status, byte healthPoint, LocalDate startedDate, long prev, long next){
        super(avatarType, name, status, startedDate, prev, next);
        this.stage = stage.name();
        this.healthPoint = healthPoint;
    }
}
