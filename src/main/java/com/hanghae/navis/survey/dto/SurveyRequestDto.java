package com.hanghae.navis.survey.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SurveyRequestDto extends BoardRequestDto {
    private long expirationDate;
}
