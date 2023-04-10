package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserAnswerResponseDto {
    private Long id;
    private String answers;

    public static UserAnswerResponseDto of(Answer answer) {
        return UserAnswerResponseDto.builder()
                .id(answer.getId())
                .answers(answer.getAnswer())
                .build();
    }
}
