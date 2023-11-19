package com.galaxy.gunpang.bodyComposition.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BodyCompositionResDto {

    private String prevWeight;
    @NonNull
    private String curWeight;
//    private float prevMuscleMass;
//    @NonNull
//    private float curMuscleMass;
    private String prevFatMass;
    @NonNull
    private String curFatMass;
    private String prevFatMassPct;
    @NonNull
    private String curFatMassPct;
    private String prevBMI;
    @NonNull
    private String curBMI;
//    private float prevBodyWaterMass;
//    @NonNull
//    private float curBodyWaterMass;
}
