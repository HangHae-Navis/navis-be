package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HomeworkRequestDto extends BoardRequestDto {
    private Long expirationDate;

    public HomeworkRequestDto(String title, String subtitle, String content, Long expirationDate) {
        super(title, subtitle, content);
        this.expirationDate = expirationDate;
    }
}
