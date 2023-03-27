package com.hanghae.navis.messenger.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
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

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "room";
    }

    // 모든 채팅방 목록 반환
    @Operation(summary = "채팅 목록", description = "채팅 목록")
    @GetMapping("lists")
    @ResponseBody
    public ResponseEntity<Message> chatList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messengerService.getChatList(userDetails.getUser());
    }
//    // 채팅방 생성
//    @PostMapping("/room")
//    @ResponseBody
//    public Messenger createRoom(@RequestParam String name) {
//        return messengerService.createRoom(name);
//    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<Message> roomInfo(@PathVariable String roomId) {
        return messengerService.getChatDetail(roomId);
    }

    @Operation(summary = "채팅 유저등록", description = "채팅하기")
    @MessageMapping("/message")
    public void enter(MessengerChatRequestDto message, @Header("Authorization") String token) {
        messengerService.sendMessage(message, message.getMessage(), token);
    }
}