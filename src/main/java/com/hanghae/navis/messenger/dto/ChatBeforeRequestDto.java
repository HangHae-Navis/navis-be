package com.hanghae.navis.messenger.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatBeforeRequestDto {
    private Long roomId;
    private String to;
    private int page;
    private int size;
}
