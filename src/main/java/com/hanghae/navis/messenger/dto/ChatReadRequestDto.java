package com.hanghae.navis.messenger.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatReadRequestDto {
    private Long roomId;
}
