package com.galaxy.gunpang.bodyComposition.model.dto;

import com.galaxy.gunpang.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyCompositionApiReqDto {

    private float weight;
    private float fatMassPct;
}
