package com.hanghae.navis.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserAnswerResponseDto {
    private Long userId;
    private List<String> answerList;

    public static UserAnswerResponseDto of(Long userId, List<String> answerList) {
        return UserAnswerResponseDto.builder()
                .userId(userId)
                .answerList(answerList)
                .build();
    }
}
