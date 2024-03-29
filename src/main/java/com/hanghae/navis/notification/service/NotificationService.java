package com.hanghae.navis.notification.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.notification.dto.NotificationResponseDto;
import com.hanghae.navis.notification.entity.Notification;
import com.hanghae.navis.notification.entity.NotificationType;
import com.hanghae.navis.notification.repository.EmitterRepository;
import com.hanghae.navis.notification.repository.NotificationQueryDslRepository;
import com.hanghae.navis.notification.repository.NotificationRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
@Slf4j
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationQueryDslRepository notificationQueryDslRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private Long DEFAULT_TIMEOUT = 60L * 1000L * 45L;

    @Transactional
    public SseEmitter subscribe(User user, String lastEventId) {
        user = userRepository.findById(user.getId()).orElseThrow(
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
    public void send(User receiver, NotificationType notificationType, String content, String url, Group group) {
        Map<String, SseEmitter> emitters = new HashMap<>() {};
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));

        if (notificationType == NotificationType.CHAT_POST) {
            notificationRepository.deleteByUserAndUrl(receiver, url);
            notificationRepository.save(createNotification(receiver, notificationType, content, url));
            emitters = emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(receiver.getId()));
        } else {
            List<GroupMember> groupMemberList = group.getGroupMember();
            List<Notification> notificationList = new ArrayList<>();
            for (GroupMember groupMember : groupMemberList) {
                if (!receiver.equals(groupMember.getUser())) {
                    notificationList.add(createNotification(groupMember.getUser(), notificationType, content, url));
                }
                emitters.putAll(emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(groupMember.getUser().getId())));
            }
            notificationRepository.saveAll(notificationList);
        }


        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
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
        List<Notification> notificationList = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        if (!notificationList.isEmpty()) {
            for (Notification notification : notificationList) {
                notificationResponseDtoList.add(NotificationResponseDto.of(notification));
            }

            notificationQueryDslRepository.updateIsReadByUserId(user.getId());
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
