package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
//@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String subtitle;
    private String content;
    private List<HashtagRequestDto> hashtagList;

    public BoardRequestDto(String title, String content, String subtitle, List<HashtagRequestDto> hashtagList) {
        this.title = title;
        this.content = content;
        this.subtitle = subtitle;
        this.hashtagList = hashtagList;
    }
}
