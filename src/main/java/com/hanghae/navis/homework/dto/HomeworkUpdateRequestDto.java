package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeworkUpdateRequestDto extends BoardUpdateRequestDto {
    private Long expirationDate;
}
