package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HomeworkRequestDto extends BoardRequestDto {
    private Long expirationDate;

    public HomeworkRequestDto(String title, String subtitle, String content, List<HashtagRequestDto> hashtagList, Long expirationDate) {
        super(title, subtitle, content, hashtagList);
        this.expirationDate = expirationDate;
    }
}
