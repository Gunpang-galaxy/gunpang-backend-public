package com.galaxy.gunpang.notification.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordNotificationReqDto {
    @NonNull
    private long userId;
    @NonNull
    private String token;
}
