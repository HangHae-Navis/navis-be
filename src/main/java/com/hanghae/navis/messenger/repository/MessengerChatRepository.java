package com.hanghae.navis.messenger.repository;

import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.MessengerChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessengerChatRepository extends JpaRepository<MessengerChat, Long> {
    List<MessengerChat> findTop1ByMessengerAndReadOrderByCreatedAt(Messenger messenger, boolean read);
    Long countByMessengerAndRead(Messenger messenger, boolean read);
    Page<MessengerChat> findByMessengerId(Long messengerId, Pageable pageable);
}
