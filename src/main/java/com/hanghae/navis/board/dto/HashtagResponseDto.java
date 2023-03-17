package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagResponseDto {
    private String tagName;

    public HashtagResponseDto(String tagName) {
        this.tagName = tagName;
    }
}
