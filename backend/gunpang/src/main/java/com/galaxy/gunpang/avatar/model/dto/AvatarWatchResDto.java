package com.galaxy.gunpang.avatar.model.dto;

import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvatarWatchResDto {
    private Long avatarTypeId;
    private Status status;
    private float healthPoint;
    private Stage stage;
}
