package com.galaxy.gunpang.user.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class LogInResDto {

    @NotNull
    private String googleId;
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;

}