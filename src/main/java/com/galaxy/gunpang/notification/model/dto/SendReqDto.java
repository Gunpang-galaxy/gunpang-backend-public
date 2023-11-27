package com.galaxy.gunpang.notification.model.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SendReqDto {

    @NonNull
    private String title;
    @NonNull
    private String body;
    @NonNull
    private String targetToken;

}
