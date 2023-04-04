package com.hanghae.navis.notification.component;

import com.hanghae.navis.notification.dto.NotificationRequestDto;
import com.hanghae.navis.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async
    public void handleNotification(NotificationRequestDto requestDto) {
        notificationService.send(requestDto.getReceiver(), requestDto.getNotificationType(),
                requestDto.getContent(), requestDto.getUrl());
    }
}