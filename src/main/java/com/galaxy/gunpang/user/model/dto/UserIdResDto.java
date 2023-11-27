package com.galaxy.gunpang.user.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class UserIdResDto {

    @NotNull
    private Long id;

}
