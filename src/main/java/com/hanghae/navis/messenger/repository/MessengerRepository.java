package com.hanghae.navis.messenger.repository;

import com.hanghae.navis.messenger.dto.MessengerListResponseDto;
import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessengerRepository extends JpaRepository<Messenger, Long> {

    @Query(value = "SELECT m.id, " +
            "       CASE WHEN m.user1_id = :user THEN m.user2_id ELSE m.user1_id END AS toUser, " +
            "       u2.username, " +
            "       CASE WHEN u.id = :user THEN u2.nickname ELSE u.nickname END AS nickname, " +
            "       CASE WHEN u.id = :user THEN u2.profile_image ELSE u.profile_image END AS profileImage, " +
            "       mc.message AS lastMessage, " +
            "       COUNT(CASE WHEN mc.read = FALSE AND mc.author_id != :user THEN 1 END) AS newMessageCount, " +
            "       MAX(mc.created_at) as createdAt " +
            "FROM messenger m " +
            "LEFT OUTER JOIN (SELECT messenger_id, message, mc1.read, author_id, created_at FROM messenger_chat mc1 WHERE id = (SELECT MAX(id) FROM messenger_chat mc2 WHERE mc2.messenger_id = mc1.messenger_id)) mc ON m.id = mc.messenger_id " +
            "LEFT OUTER JOIN users u ON m.user1_id = u.id OR m.user2_id = u.id " +
            "LEFT OUTER JOIN users u2 ON u2.id = CASE WHEN m.user1_id = :user THEN m.user2_id ELSE m.user1_id END " +
            "WHERE u.id = :user " +
            "GROUP BY m.id " +
            "ORDER BY createdAt DESC", nativeQuery = true)
    List<MessengerListResponseDto> findByMessengerList(User user);

    List<Messenger> findByUser1OrUser2(User user1, User user2);
}
