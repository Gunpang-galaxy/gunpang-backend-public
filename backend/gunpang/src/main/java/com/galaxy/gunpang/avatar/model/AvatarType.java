package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AvatarType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 6, nullable = false)
    private String defaultName;
    @Column(length = 16, nullable = false)
    private String defaultImg;

    @Builder
    public AvatarType(Long id, String defaultName, String defaultImg){
        this.id = id;
        this.defaultName = defaultName;
        this.defaultImg = defaultImg;
    }
}
