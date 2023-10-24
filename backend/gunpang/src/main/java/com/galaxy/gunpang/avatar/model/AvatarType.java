package com.galaxy.gunpang.avatar.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AvatarType extends BaseEntity {
    @Id
    private Long id;
    @Column(length = 6)
    private String default_name;
    @Lob
    @Column(length=100000)
    private byte[] default_img;
}
