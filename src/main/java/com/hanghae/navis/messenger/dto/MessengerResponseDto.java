package com.hanghae.navis.messenger.dto;

import com.hanghae.navis.messenger.entity.MessengerChat;
import com.hanghae.navis.user.entity.User;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessengerResponseDto {

    private Long roomId;
    private Long id;
    private String authorName;
    private String message;
    private boolean read;
    private LocalDateTime created_at;
    public static MessengerResponseDto of(MessengerChat messengerChat, User me) {
        return MessengerResponseDto.builder()
                .roomId(messengerChat.getMessenger().getId())
                .id(messengerChat.getId())
                .authorName(messengerChat.getAuthor().getUsername())
                .message(messengerChat.getMessage())
                .read(messengerChat.isRead())
                .created_at(messengerChat.getCreatedAt())
                .build();
    }

    public static Page<MessengerResponseDto> toDtoPage(Page<MessengerChat> messengerChat, User me) {
        return messengerChat.map(value -> MessengerResponseDto.of(value, me));
    }
}
