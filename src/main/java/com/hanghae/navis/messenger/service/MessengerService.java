package com.hanghae.navis.messenger.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.messenger.dto.*;
import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.MessengerChat;
import com.hanghae.navis.messenger.repository.MessengerChatRepository;
import com.hanghae.navis.messenger.repository.MessengerQueryDslRepository;
import com.hanghae.navis.messenger.repository.MessengerRepository;
import com.hanghae.navis.notification.entity.NotificationType;
import com.hanghae.navis.notification.service.NotificationService;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessengerService {
    private final UserRepository userRepository;
    private final MessengerRepository messengerRepository;
    private final MessengerQueryDslRepository messengerQueryDslRepository;
    private final MessengerChatRepository messengerChatRepository;
    private final NotificationService notificationService;
    private EntityManager entityManager;

    private final SimpMessageSendingOperations sendingOperations;
    private final JwtUtil jwtUtil;


    public static LinkedList<ChatingRoom> chatingRoomList = new LinkedList<>();

    @Transactional(readOnly = true)
    //채팅방 불러오기
    public ResponseEntity<Message> getChatList(User user) {
        //유저 체크
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //내 대화방 목록 가져오기
        List<MessengerListResponseDto> room = messengerRepository.findByMessengerList(me);

        return Message.toResponseEntity(CHAT_LIST_GET_SUCCESS, room);
    }

    //채팅방 하나 불러오기
    @Transactional(readOnly = true)
    public ResponseEntity<Message> getChatDetail(ChatBeforeRequestDto requestDto, User user) {
        User toUser = userRepository.findByUsername(requestDto.getTo()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //메신저에 대화방이 있는지 체크
        Messenger room = messengerRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new CustomException(CHAT_ROOM_NOT_FOUND)
        );

        Pageable pageable = PageRequest.of(requestDto.getPage() - 1, requestDto.getSize(), Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<MessengerChat> messengerChatPage = messengerChatRepository.findByMessengerIdOrderByCreatedAt(room.getId(), pageable);
        Page<MessengerResponseDto> messengerResponseDto = MessengerResponseDto.toDtoPage(messengerChatPage, me);

        return Message.toResponseEntity(CHAT_ENTER_SUCCESS, messengerResponseDto);
    }

    //채팅방 생성
    @Transactional
    public ResponseEntity<Message> createRoom(ChatCreateRequestDto requestDto, User user) {
        if (requestDto.getTo().equals(user.getUsername())) {
            throw new CustomException(CANNOT_CHAT_MYSELF);
        }

        User toUser = userRepository.findByUsername(requestDto.getTo()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Messenger room = null;
        Optional<Messenger> messenger = messengerQueryDslRepository.findByMessenger(me, toUser);

        //메신저에 대화방이 있는지 체크
        if (messenger.isPresent()) {
            room = messenger.get();
        }

        //메신저에 등록된게 없다면 1:1대화방 만들어줌
        if (room == null) {
            room = new Messenger(me, toUser);
            messengerRepository.save(room);
        }

        return Message.toResponseEntity(CHAT_ROOM_CREATE_SUCCESS, room.getId());
    }


    //메세지 보내기
    @Transactional
    public ResponseEntity<Message> sendMessage(MessengerChatRequestDto requestDto, String message, String token) {
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();
        User toUser = userRepository.findByUsername(requestDto.getTo()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        User me = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //메신저에 대화방이 있는지 체크
        Messenger room = messengerQueryDslRepository.findByMessenger(me, toUser).orElseThrow(
                () -> new CustomException(CHAT_ROOM_NOT_FOUND)
        );


        if (requestDto.getType() == MessengerChatRequestDto.MessageType.TALK) {
            MessengerChat messengerChat = new MessengerChat(message, false, me, room);
            messengerChatRepository.save(messengerChat);
            sendingOperations.convertAndSend("/chats/room/" + room.getId(), MessengerResponseDto.of(messengerChat, me));

            notificationService.send(toUser, NotificationType.CHAT_POST, me.getNickname() + "님 에게 " + NotificationType.CHAT_POST.getContent(), me.getNickname(), null);
        }
        return Message.toResponseEntity(CHAT_POST_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> readChat(ChatReadRequestDto requestDto, User user) {
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //메신저에 대화방이 있는지 체크
        Messenger room = messengerRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new CustomException(CHAT_ROOM_NOT_FOUND)
        );

        messengerQueryDslRepository.updateRead(room.getId(), me.getId());

        return Message.toResponseEntity(CHAT_READ_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> deleteMessenger(String roomId, User user) {
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //메신저에 대화방이 있는지 체크
        Messenger room = messengerRepository.findById(Long.parseLong(roomId)).orElseThrow(
                () -> new CustomException(CHAT_ROOM_NOT_FOUND)
        );

        messengerRepository.delete(room);

        return Message.toResponseEntity(CHAT_ROOM_DELETE_SUCCESS);
    }

    //회원탈퇴 전용
    @Transactional
    public boolean deleteLeaveMessenger(User user) {
        try {
            User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            //메신저에 대화방이 있는지 체크
            List<Messenger> messengerList = messengerRepository.findByUser1OrUser2(me, me);
            if (!messengerList.isEmpty()) {
                for (Messenger messenger : messengerList) {
                    messengerRepository.delete(messenger);
                }
            }
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

}
