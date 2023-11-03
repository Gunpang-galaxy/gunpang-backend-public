package com.galaxy.gunpang.goal.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResDto {
    private List<DamageResDto> data;
}
