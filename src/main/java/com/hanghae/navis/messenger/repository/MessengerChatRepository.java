package com.hanghae.navis.messenger.repository;

import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.MessengerChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessengerChatRepository extends JpaRepository<MessengerChat, Long> {
    List<MessengerChat> findTop1ByMessengerAndReadOrderByCreatedAt(Messenger messenger, boolean read);

    Long countByMessengerAndRead(Messenger messenger, boolean read);

    Page<MessengerChat> findByMessengerIdOrderByCreatedAt(Long messengerId, Pageable pageable);


    @Query(value = "UPDATE messenger_chat " +
            "SET read = 'true' " +
            "WHERE messenger_id = :roomId AND author_id != :userId "
            , nativeQuery = true)
    void updateRead(Long roomId, Long userId);
}
