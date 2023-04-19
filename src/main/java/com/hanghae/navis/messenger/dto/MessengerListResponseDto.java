package com.hanghae.navis.messenger.dto;

import com.hanghae.navis.messenger.entity.MessengerChat;
import lombok.*;

import java.time.LocalDateTime;

public interface MessengerListResponseDto {
     Long getId();
     Long getToUser();
     String getUsername();
     String getNickname();
     String getProfileImage();
     String getLastMessage();
     Long getNewMessageCount();
     LocalDateTime getCreatedAt();

}
