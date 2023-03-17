package com.hanghae.navis.common.dto;

import com.hanghae.navis.common.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class HashtagResponseDto {
    private String tagName;

    public static HashtagResponseDto of(Hashtag hashtag) {
        return HashtagResponseDto.builder()
                .tagName(hashtag.getHashtagName())
                .build();
    }
}
