package com.galaxy.gunpang.user.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class SignUpReqDto {

    private String gender;
    private int birthYear;
    private int height;
    private String googleId;
    private String email;

}
