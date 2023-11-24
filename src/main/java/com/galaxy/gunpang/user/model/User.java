package com.galaxy.gunpang.user.model;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.common.model.BaseEntity;
import com.galaxy.gunpang.user.model.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "USER")
public class User extends BaseEntity {
    @Id
    private Long id;

    @Column(length = 30, nullable = false)
    private String googleId;

    @Column(length = 40, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(columnDefinition = "smallint", nullable = false)
    private int birthYear;

    @Column(columnDefinition = "smallint", nullable = false)
    private int height;

    @Column(columnDefinition = "tinyint", nullable = false)
    private int isDeleted = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BodyComposition> bodyCompositions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Avatar> avatars;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", googleId='" + googleId + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", birthYear=" + birthYear +
                ", height=" + height +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public void updateUserToDeleted() {
        this.googleId = "deleted";
        this.email = "deleted";
        this.gender = Gender.NONE;
        this.birthYear = 0;
        this.height = 0;
        this.isDeleted = 1;
    }

}
