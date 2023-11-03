package com.galaxy.gunpang.user.model.dto;

import com.galaxy.gunpang.user.model.enums.Gender;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResDto {

    @NonNull
    private String email;
    @NonNull
    private int birthYear;
    @NonNull
    private int height;
    @NonNull
    private Gender gender;

}
