package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.avatar.model.enums.Cause;
import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@RequiredArgsConstructor
public class DeathCause extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Avatar avatar;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(16)", nullable = false)
    private Cause cause;
    private LocalDate date;

    @Builder
    public DeathCause(Long id, Avatar avatar, Cause cause, LocalDate date){
        this.id = id;
        this.avatar = avatar;
        this.cause = cause;
        this.date = date;
    }
}
