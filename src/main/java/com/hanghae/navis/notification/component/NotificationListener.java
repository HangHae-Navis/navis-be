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

    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;

    @TransactionalEventListener
    @Async
    public void handleNotification(NotificationRequestDto requestDto) {
        notificationService.send(requestDto.getReceiver(), requestDto.getNotificationType(),
                requestDto.getContent(), requestDto.getUrl());
    }

    @Scheduled(fixedDelay = 30000)
    public void checkEmitters() {
        // reconnect logic
        emitterRepository.closeAllEmitters();
    }
}