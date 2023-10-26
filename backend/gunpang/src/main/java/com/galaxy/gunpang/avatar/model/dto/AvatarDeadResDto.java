package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.AvatarType;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvatarDeadResDto extends AvatarResDto{
    private String finishedDate;
    private String deathCause;

    @Builder
    public AvatarDeadResDto(AvatarType avatarType, String name, Status status, LocalDate startedDate, LocalDate finishedDate, DeathCause deathCause, long prev, long next){
        super(avatarType, name, status, startedDate, prev, next);
        this.finishedDate = finishedDate.toString();
        this.deathCause = deathCause.getCause().name();
    }
}
