package com.hanghae.navis.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerRequestDto {
    private Long questionId;
    private List<String> answerList;

    public AnswerRequestDto(Long questionId, List<String> answerList) {
        this.questionId = questionId;
        this.answerList = answerList;
    }
}
