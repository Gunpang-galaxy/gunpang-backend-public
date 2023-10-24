package com.galaxy.gunpang.bodyComposition.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "BODY_COMPOSITION")
public class BodyComposition extends BaseEntity {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "float", nullable = false)
    private float weight;

    @Column(columnDefinition = "float", nullable = false)
    private float muscleMass;

    @Column(columnDefinition = "float", nullable = false)
    private float fatMass;

    @Column(columnDefinition = "float", nullable = false)
    private float fatMassPct;

    @Column(columnDefinition = "float", nullable = false)
    private float bodyWaterMass;
}