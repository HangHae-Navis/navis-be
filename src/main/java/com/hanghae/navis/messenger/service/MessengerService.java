package com.hanghae.navis.messenger.service;

import com.hanghae.navis.comment.dto.CommentResponseDto;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.messenger.dto.ChatingRoom;
import com.hanghae.navis.messenger.dto.MessengerChatRequestDto;
import com.hanghae.navis.messenger.dto.MessengerListResponseDto;
import com.hanghae.navis.messenger.dto.MessengerResponseDto;
import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.MessengerChat;
import com.hanghae.navis.messenger.repository.MessengerChatRepository;
import com.hanghae.navis.messenger.repository.MessengerRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

import static com.hanghae.navis.common.entity.ExceptionMessage.CHAT_ROOM_NOT_FOUND;
import static com.hanghae.navis.common.entity.ExceptionMessage.MEMBER_NOT_FOUND;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessengerService {
    JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private final MessengerRepository messengerRepository;
    private final MessengerChatRepository messengerChatRepository;
    private EntityManager entityManager;

    private final SimpMessageSendingOperations sendingOperations;
    private final JwtUtil jwtUtil;

    public static LinkedList<ChatingRoom> chatingRoomList = new LinkedList<>();

    //채팅방 불러오기
    public ResponseEntity<Message> getChatList(User user) {
        //유저 체크
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //내 대화방 목록 가져오기
        List<MessengerListResponseDto> room = messengerRepository.findByMessengerList(me);

        return Message.toResponseEntity(BOARD_POST_SUCCESS, room);
    }

    //채팅방 하나 불러오기
    public ResponseEntity<Message> getChatDetail(String roomId) {
        return Message.toResponseEntity(BOARD_POST_SUCCESS);
    }

    //채팅방 생성
    public ResponseEntity<Message> createRoom(String to, User user) {
        User toUser = userRepository.findByUsername(to).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Messenger room = null;
        Optional<Messenger> messenger = messengerRepository.findByMessenger(me, toUser);

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
        Messenger room = messengerRepository.findByMessenger(me, toUser).orElseThrow(
                () -> new CustomException(CHAT_ROOM_NOT_FOUND)
        );

        //만약 입장이라면 채팅기록을 보내주고 입력이면 채팅을 보내줌
        if (requestDto.getType() == MessengerChatRequestDto.MessageType.ENTER) {
            Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize() + requestDto.getNewMessageCount());
            Page<MessengerChat> messengerChatPage = messengerChatRepository.findByMessengerId(room.getId(), pageable);
            Page<MessengerResponseDto> messengerResponseDto = MessengerResponseDto.toDtoPage(messengerChatPage, me);
            sendingOperations.convertAndSend("/chats/room/" + room.getId(), messengerResponseDto);
            return Message.toResponseEntity(CHAT_ENTER_SUCCESS);
        } else if (requestDto.getType() == MessengerChatRequestDto.MessageType.TALK) {
            MessengerChat messengerChat = new MessengerChat(message, false, me, room);
            messengerChatRepository.save(messengerChat);
            sendingOperations.convertAndSend("/chats/room/" + room.getId(), MessengerResponseDto.of(messengerChat, me));
        }
        return Message.toResponseEntity(CHAT_POST_SUCCESS);
    }
}
