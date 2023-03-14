package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String content;

    public BoardRequestDto(String content) {
        this.content = content;
    }
}
