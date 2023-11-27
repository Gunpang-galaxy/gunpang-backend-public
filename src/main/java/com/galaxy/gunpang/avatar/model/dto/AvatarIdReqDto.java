package com.galaxy.gunpang.avatar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AvatarIdReqDto {
    @NonNull
    private Long avatarId;
}
