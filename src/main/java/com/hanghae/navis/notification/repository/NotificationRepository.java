package com.hanghae.navis.notification.repository;

import com.hanghae.navis.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
