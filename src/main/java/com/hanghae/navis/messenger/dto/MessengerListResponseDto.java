package com.hanghae.navis.messenger.dto;

import com.hanghae.navis.messenger.entity.MessengerChat;
import lombok.*;

import java.time.LocalDateTime;

public interface MessengerListResponseDto {
     Long getId();
     String getLastMessage();
     Long getNewMessageCount();
     LocalDateTime getCreatedAt();

}
