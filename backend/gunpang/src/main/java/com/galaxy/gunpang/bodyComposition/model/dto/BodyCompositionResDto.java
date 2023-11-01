package com.galaxy.gunpang.bodyComposition.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BodyCompositionResDto {

    private float prevWeight;
    @NonNull
    private float curWeight;
    private float prevMuscleMass;
    @NonNull
    private float curMuscleMass;
    private float prevFatMass;
    @NonNull
    private float curFatMass;
    private float prevFatMassPct;
    @NonNull
    private float curFatMassPct;
    private float prevBMI;
    @NonNull
    private float curBMI;
    private float prevBodyWaterMass;
    @NonNull
    private float curBodyWaterMass;
}
