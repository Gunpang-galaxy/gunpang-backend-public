package com.galaxy.gunpang.bodyComposition.service;

import com.galaxy.gunpang.bodyComposition.model.dto.BodyCompositionResDto;

public interface BodyCompositionService {
    void setBodyCompositionsWithHealthApi(Long userId, float weight, float muscleMass, float fatMass, float bodyWaterMass);

    BodyCompositionResDto getBodyCompositions(Long userId);
}
