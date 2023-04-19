package com.hanghae.navis.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormRequestDto {
    private List<AnswerRequestDto> answerRequestDto;

    public FormRequestDto(List<AnswerRequestDto> answerRequestDto) {
        this.answerRequestDto = answerRequestDto;
    }
}
