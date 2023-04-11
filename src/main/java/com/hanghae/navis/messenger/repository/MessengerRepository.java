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
            "       u2.username AS username, " +
            "       CASE WHEN u.id = :user THEN u2.nickname ELSE u.nickname END AS nickname, " +
            "       CASE WHEN u.id = :user THEN u2.profile_image ELSE u.profile_image END AS profileImage, " +
            "       (SELECT message FROM messenger_chat WHERE m.id = messenger_chat.messenger_id ORDER BY id DESC LIMIT 1) AS lastMessage, " +
            "       (SELECT DISTINCT (SELECT COUNT(*) FROM messenger_chat WHERE messenger_chat.read = FALSE AND messenger_chat.messenger_id = m.id AND messenger_chat.author_id != :user) FROM messenger_chat) AS newMessageCount, " +
            "       mc.created_at as createdAt " +
            "       FROM messenger m " +
            "       LEFT OUTER JOIN messenger_chat mc ON m.id = mc.messenger_id " +
            "       LEFT OUTER JOIN users u ON m.user1_id = u.id OR m.user2_id = u.id " +
            "       LEFT OUTER JOIN users u2 ON u2.id = CASE WHEN m.user1_id = :user THEN m.user2_id ELSE m.user1_id END " +
            "       WHERE u.id = :user " +
            "       GROUP BY m.id"
            , nativeQuery = true)
    List<MessengerListResponseDto> findByMessengerList(User user);


    @Query(value = "SELECT * " +
            "FROM messenger " +
            "WHERE (user1_id = :user1 AND user2_id = :user2) " +
            "OR (user1_id = :user2 AND user2_id = :user1) "
            , nativeQuery = true)
    Optional<Messenger> findByMessenger(User user1, User user2);

    List<Messenger> findByUser1OrUser2(User user1, User user2);
}
