package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private List<String> answerList;

    public static AnswerResponseDto of(Answer answer, List<String> answerList) {
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .answerList(answerList)
                .build();
    }
}
