package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.Survey;
import com.hanghae.navis.survey.entity.SurveyQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SurveyResponseDto {
    private long id;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private String title;
    private List<QuestionResponseDto> questionResponseDto;

    public static SurveyResponseDto of(Survey survey, List<QuestionResponseDto> questionResponseDto) {
        return SurveyResponseDto.builder()
                .id(survey.getId())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .title(survey.getTitle())
                .questionResponseDto(questionResponseDto)
                .build();
    }
}
