package com.galaxy.gunpang.notification.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import com.galaxy.gunpang.notification.model.dto.FcmMessage.Message;
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "NOTIFICATION")
public class Notification {

    @GeneratedValue
    private long id;

    @Column
    private long userId;

    @Column(length = 163)
    //특정 device에 알림을 보내는 용도
    private String token;

}
