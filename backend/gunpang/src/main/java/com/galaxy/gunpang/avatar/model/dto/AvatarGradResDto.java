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
public class AvatarGradResDto extends AvatarResDto{
    private String finishedDate;
    private String stage;

    @Builder
    public AvatarGradResDto(AvatarType avatarType, String name, Stage stage, Status status, LocalDate startedDate, LocalDate finishedDate, long prev, long next){
        super(avatarType, name, status, startedDate, prev, next);
        this.finishedDate = finishedDate.toString();
        this.stage = stage.name();
    }
}
