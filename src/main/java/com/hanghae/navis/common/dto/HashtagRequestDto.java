package com.hanghae.navis.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HashtagRequestDto {
    private String hashtag;

    public HashtagRequestDto(String hashtag) {
        this.hashtag = hashtag;
    }
}
