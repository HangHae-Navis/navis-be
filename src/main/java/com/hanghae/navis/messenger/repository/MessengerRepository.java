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
            "(SELECT message FROM messenger_chat WHERE m.id = messenger_chat.messenger_id ORDER BY id DESC LIMIT 1) as lastMessage, " +
            "(SELECT DISTINCT (SELECT COUNT(*) FROM messenger_chat WHERE messenger_chat.read = FALSE) FROM messenger_chat) AS newMessageCount, " +
            "mc.created_at " +
            "FROM messenger m " +
            "LEFT OUTER JOIN messenger_chat mc ON m.id = mc.id " +
            "WHERE mc.author_id = :user"
            , nativeQuery = true)
    List<MessengerListResponseDto> findByMessengerList(User user);


    @Query(value = "SELECT * " +
            "FROM messenger " +
            "WHERE (user1_id = :user1 AND user2_id = :user2) " +
            "   OR (user1_id = :user2 AND user2_id = :user1) ", nativeQuery = true)
    Optional<Messenger> findByMessenger(User user1, User user2);
}
