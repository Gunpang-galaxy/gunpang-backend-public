package com.galaxy.gunpang.bodyComposition.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Table(name = "BODY_COMPOSITION")
public class BodyComposition extends BaseEntity {
    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public BodyComposition(User user, float weight, float muscleMass, float fatMass, float fatMassPct, float bodyWaterMass) {
        this.user = user;
        this.weight = weight;
        this.bodyWaterMass = bodyWaterMass;
        this.muscleMass = muscleMass;
        this.fatMass = fatMass;
        this.fatMassPct = fatMassPct;
    }
}