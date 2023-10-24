package com.galaxy.gunpang.user.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.*;

import javax.persistence.*;

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

}
