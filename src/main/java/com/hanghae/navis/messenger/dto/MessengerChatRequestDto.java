package com.hanghae.navis.messenger.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 100, message = "채팅내용은 최대 1글자부터 100글자까지 가능합니다.")
    private String message;
    private int newMessageCount;
    private int page;
    private int size;
}
