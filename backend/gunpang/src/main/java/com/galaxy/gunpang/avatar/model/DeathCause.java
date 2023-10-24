package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.avatar.model.enums.Cause;
import lombok.Builder;
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
    @JoinColumn(nullable = false)
    private Avatar avatar;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(16)", nullable = false)
    private Cause cause;

    @Builder
    public DeathCause(Long id, Avatar avatar, Cause cause){
        this.id = id;
        this.avatar = avatar;
        this.cause = cause;
    }
}
