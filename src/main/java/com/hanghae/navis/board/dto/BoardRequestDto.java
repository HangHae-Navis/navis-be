package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String subtitle;
    private String content;

    public BoardRequestDto(String content, String subtitle) {
        this.content = content;
        this.subtitle = subtitle;
    }
}
