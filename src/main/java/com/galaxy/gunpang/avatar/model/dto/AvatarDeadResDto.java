package com.galaxy.gunpang.avatar.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarDeadResDto implements AvatarContents{
    private String deathCause;

    @Builder
    public AvatarDeadResDto(String deathCause){
        this.deathCause = deathCause;
    }
}
