package com.hanghae.navis.common.dto;

import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.vote.dto.VoteListResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

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
    public static List<HashtagResponseDto> toDtoList(List<Hashtag> hashtagList) {
        List<HashtagResponseDto> hashtagResponseDtoList = new ArrayList<>();
        hashtagList.forEach(value -> hashtagResponseDtoList.add(HashtagResponseDto.of(value)));
        return hashtagResponseDtoList;
    }
}
