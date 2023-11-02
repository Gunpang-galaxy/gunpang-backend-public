package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.avatar.model.enums.Stage;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Avatar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(length = 20, nullable = false)
    private String avatarType;
    @Column(length = 6, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(8)", nullable = false)
    private Stage stage;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(16)", nullable = false)
    private Status status;
    @Column(nullable = false)
    private byte healthPoint;
    @Column(nullable = false)
    private LocalDate startedDate;
    private LocalDate finishedDate;
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeathCause> deathCauses;
    @OneToOne(mappedBy = "avatar", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Goal goal;

    public void setName(String name){
        this.name = name;
    }
    public void setHealthPoint(byte healthPoint){this.healthPoint = healthPoint;}
    public void setStatus(Status status){this.status = status;}
    public void setFinishedDate(LocalDate finishedDate) {this.finishedDate = finishedDate;}
    public void setStage(Stage stage){this.stage = stage;}

    @Builder
    public Avatar(Long id, User user, String avatarType, String name, Stage stage, Status status, byte healthPoint, LocalDate startedDate, LocalDate finishedDate){
        this.id = id;
        this.user = user;
        this.avatarType = avatarType;
        this.name = name;
        this.stage = stage;
        this.status = status;
        this.healthPoint = healthPoint;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
//        this.deathCauses = deathCauses;
//        this.goal = goal;
    }
}
