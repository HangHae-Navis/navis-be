package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String subtitle;
    private String content;

    public BoardRequestDto(String title, String content, String subtitle) {
        this.title = title;
        this.content = content;
        this.subtitle = subtitle;
    }
}
