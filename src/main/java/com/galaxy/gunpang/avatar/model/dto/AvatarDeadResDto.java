package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.DeathCause;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvatarDeadResDto implements AvatarContents{
    private String deathCause;

    @Builder
    public AvatarDeadResDto(String deathCause){
        this.deathCause = deathCause;
    }
}
