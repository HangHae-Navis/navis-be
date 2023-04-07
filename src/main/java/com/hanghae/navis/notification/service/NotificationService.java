package com.hanghae.navis.notification.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.notification.dto.NotificationResponseDto;
import com.hanghae.navis.notification.entity.Notification;
import com.hanghae.navis.notification.entity.NotificationType;
import com.hanghae.navis.notification.repository.EmitterRepository;
import com.hanghae.navis.notification.repository.NotificationRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hanghae.navis.common.entity.ExceptionMessage.MEMBER_NOT_FOUND;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;
    @Transactional
    public SseEmitter subscribe(User user, String lastEventId) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        Long memberId = user.getId();
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }


    @Transactional
    public void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data, MediaType.APPLICATION_JSON));

        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }


    @Transactional
    public void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Transactional
    public void send(User receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.of(notification));
                }
        );
    }

    private Notification createNotification(User receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }

    @Transactional
    public ResponseEntity<Message> getNotification(User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
        List<Notification> notificationList = notificationRepository.findByUserOrderByCreatedAt(user);

        if (!notificationList.isEmpty()) {
            for (Notification notification : notificationList) {
                notificationResponseDtoList.add(NotificationResponseDto.of(notification));
            }

            notificationRepository.updateIsReadByUserId(user.getId());
        }
        return Message.toResponseEntity(NOTIFICATION_GET_SUCCESS, notificationResponseDtoList);
    }

    @Transactional
    public ResponseEntity<Message> deleteNotification(String notificationId, User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        notificationRepository.deleteById(Long.parseLong(notificationId));

//        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
//        for(Notification notification : notificationRepository.findByUserOrderByCreatedAt(user)){
//            notificationResponseDtoList.add(NotificationResponseDto.of(notification));
//        }

        return Message.toResponseEntity(NOTIFICATION_DELETE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> deleteAllNotification(User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        notificationRepository.deleteByUser(user);

//        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
//        for(Notification notification : notificationRepository.findByUserOrderByCreatedAt(user)){
//            notificationResponseDtoList.add(NotificationResponseDto.of(notification));
//        }

        return Message.toResponseEntity(NOTIFICATION_DELETE_SUCCESS);
    }
}
