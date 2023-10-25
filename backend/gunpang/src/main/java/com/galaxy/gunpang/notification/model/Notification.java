package com.galaxy.gunpang.notification.model;

import com.galaxy.gunpang.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false, length = 163)
    private String firebaseToken;

    @Column(nullable = false)
    private Long userId;

}
