package com.galaxy.gunpang.avatar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class DeathCause {
    @Id
    private Long id;
    @OneToOne
    @JoinColumn
    private Avatar avatar;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(16)")
    private String cause;
}
