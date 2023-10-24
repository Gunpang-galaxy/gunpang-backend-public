package com.galaxy.gunpang.bodyComposition.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.user.model.User;
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
}