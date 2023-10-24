package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Avatar extends BaseEntity {
    @Id
    private Long id;
    private Long userId;
    @ManyToOne
    @JoinColumn(nullable = false)
    private AvatarType avatarType;
    @Column(length = 6, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(8)", nullable = false)
    private Stage stage;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(16)", nullable = false)
    private Status status;
    @Column(columnDefinition = "TINYINT", nullable = false)
    private int healthPoint;
    @Column(nullable = false)
    private LocalDate startedDate;
    private LocalDate finishedDate;

    @Builder
    public Avatar(Long id, Long userId, AvatarType avatarType, String name, Stage stage, Status status, int healthPoint, LocalDate startedDate, LocalDate finishedDate){
        this.id = id;
        this.userId = userId;
        this.avatarType = avatarType;
        this.name = name;
        this.stage = stage;
        this.status = status;
        this.healthPoint = healthPoint;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
    }
}
