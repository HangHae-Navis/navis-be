package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.entity.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagResponseDto {
    private Long id;
    private String tagName;

    public HashtagResponseDto(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.tagName = hashtag.getHashtagName();
    }
}
