package com.galaxy.gunpang.bodyComposition.service;

public interface BodyCompositionService {
    void setBodyCompositionsWithHealthApi(Long userId, float weight, float muscleMass, float fatMass, float bodyWaterMass);
}
