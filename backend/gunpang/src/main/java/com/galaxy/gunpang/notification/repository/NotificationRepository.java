package com.galaxy.gunpang.notification.repository;

import com.galaxy.gunpang.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByToken(String token);

}
