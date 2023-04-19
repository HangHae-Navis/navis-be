package com.hanghae.navis.notification.component;

import com.hanghae.navis.notification.dto.NotificationRequestDto;
import com.hanghae.navis.notification.repository.EmitterRepository;
import com.hanghae.navis.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final EmitterRepository emitterRepository;

    @Scheduled(fixedDelay = 44000)
    public void checkEmitters() {
        // reconnect logic
        emitterRepository.closeAllEmitters();
    }
}