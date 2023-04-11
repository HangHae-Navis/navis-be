package com.hanghae.navis.notification.repository;

import com.hanghae.navis.notification.entity.Notification;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    void deleteByUser(User user);
    void deleteByUserAndUrl(User user, String url);
    @Modifying
    @Query(value = "UPDATE notification SET is_read = TRUE WHERE user_id = :userId"
            , nativeQuery = true)
    void updateIsReadByUserId(Long userId);
}
