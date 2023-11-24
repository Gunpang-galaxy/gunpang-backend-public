package com.galaxy.gunpang.avatar.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@ToString
public class AvatarAddReqDto {
    @NotNull(message = "[필수] 아바타 타입")
    private String avatarType;
    @NotBlank(message = "[필수] 아바타 이름(1-6)")
    private String name;

    public AvatarAddReqDto(String avatarType, String name){
        this.avatarType = avatarType;
        this.name = name;
    }
}
