package com.hanghae.navis.messenger.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessengerChatRequestDto {
    public enum MessageType {
        ENTER, TALK, OUT
    }

    private MessageType type;
    private String to;
    private String message;
    private int newMessageCount;
    private int page;
    private int size;
}
