package com.hanghae.navis.common.dto;

import com.hanghae.navis.common.entity.File;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    @AllArgsConstructor
    public enum MessageType {
        HOMEWORK("그룹에 과제가 새로 등록 되었습니다."),
        MESSENGER("chat");

        String message;
    }

    private MessageType messageType;

    public static NotificationResponseDto of(MessageType messageType) {
        return NotificationResponseDto.builder()
                .messageType(messageType)
                .build();
    }
}
