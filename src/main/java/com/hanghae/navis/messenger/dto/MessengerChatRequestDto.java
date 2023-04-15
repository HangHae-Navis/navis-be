package com.hanghae.navis.messenger.dto;

import lombok.*;

import javax.validation.constraints.Min;

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
    @Min(value = 1)
    private String message;
    private int newMessageCount;
    private int page;
    private int size;
}
