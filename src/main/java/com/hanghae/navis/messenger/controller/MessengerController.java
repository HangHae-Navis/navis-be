package com.hanghae.navis.messenger.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.messenger.dto.ChatBeforeRequestDto;
import com.hanghae.navis.messenger.dto.ChatCreateRequestDto;
import com.hanghae.navis.messenger.dto.MessengerChatRequestDto;
import com.hanghae.navis.messenger.service.MessengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "chats")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class MessengerController {
    private final MessengerService messengerService;

    @GetMapping("/chat-page")
    public ModelAndView chatPage() {
        return new ModelAndView("chatroom");
    }

    // 모든 채팅방 목록 반환
    @Operation(summary = "채팅 목록", description = "채팅 목록")
    @GetMapping("lists")
    @ResponseBody
    public ResponseEntity<Message> chatList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messengerService.getChatList(userDetails.getUser());
    }
    // 채팅방 생성
    @Operation(summary = "채팅방 생성", description = "채팅방 생성")
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<Message> createRoom(@RequestBody ChatCreateRequestDto requestDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messengerService.createRoom(requestDto, userDetails.getUser());
    }

    // 특정 채팅방 조회
    @Operation(summary = "이전 채팅 가져오기", description = "이전 채팅 가져오기")
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<Message> roomInfo(@PathVariable(value = "roomId") String roomId, @RequestParam String to, @RequestParam int page, @RequestParam int size, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatBeforeRequestDto requestDto = new ChatBeforeRequestDto(Long.parseLong(roomId) ,to, page, size);
        return messengerService.getChatDetail(requestDto, userDetails.getUser());
    }
    
    // 채팅 읽음처리
    @Operation(summary = "읽음 처리", description = "읽음 처리")
    @PostMapping("/room/{roomId}/read")
    @ResponseBody
    public ResponseEntity<Message> readChat(@RequestBody ChatBeforeRequestDto requestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messengerService.getChatDetail(requestDto, userDetails.getUser());
    }

    @Operation(summary = "채팅하기", description = "채팅하기")
    @MessageMapping("/message")
    public void enter(MessengerChatRequestDto message, @Header("Authorization") String token) {
        messengerService.sendMessage(message, message.getMessage(), token);
    }
}