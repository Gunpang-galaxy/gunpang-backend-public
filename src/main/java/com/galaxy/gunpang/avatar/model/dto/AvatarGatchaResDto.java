package com.galaxy.gunpang.avatar.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarGatchaResDto {
    private Long avatarId;
    private String defaultName;
    private String defaultImg;

    @Builder
    public AvatarGatchaResDto(Long avatarId, String defaultName, String defaultImg){
        this.avatarId = avatarId;
        this.defaultName = defaultName;
        this.defaultImg = defaultImg;
    }
}
