package com.galaxy.gunpang.avatar.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@ToString
public class AvatarNamingReqDto {
    @NotNull(message = "[필수] 아바타 아이디")
    private Long avatarId;
    @NotBlank(message = "[필수] 아바타 이름(1-6)")
    private String name;

    public AvatarNamingReqDto(Long avatarId, String name){
        this.avatarId = avatarId;
        this.name = name;
    }
}
