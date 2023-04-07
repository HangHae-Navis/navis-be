package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.SurveyOption;
import com.hanghae.navis.survey.entity.SurveyQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String type;
    private String question;
    private List<String> optionList;

    public static QuestionResponseDto of(SurveyQuestion surveyQuestion, List<String> optionList) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .question(surveyQuestion.getQuestion())
//                .optionList(surveyQuestion.getOptionList().stream()
//                        .map(SurveyOption::getOption)
//                        .collect(Collectors.toList()))
                .optionList(optionList)
                .build();
    }
}
