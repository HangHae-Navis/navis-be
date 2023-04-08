package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.Answer;
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
    private Long number;
    private String question;
    private List<String> optionList;
    private List<String> answerList;

    public static QuestionResponseDto of(SurveyQuestion surveyQuestion, List<String> optionList) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionList(optionList)
                .build();
    }

    public static QuestionResponseDto getOf(SurveyQuestion surveyQuestion) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionList(surveyQuestion.getOptionList().stream()
                        .map(SurveyOption::getOption)
                        .collect(Collectors.toList()))
                .build();
    }

    public static QuestionResponseDto submitTrueOf(SurveyQuestion surveyQuestion) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionList(surveyQuestion.getOptionList().stream()
                        .map(SurveyOption::getOption)
                        .collect(Collectors.toList()))
//                .answerList(answerList)
                .answerList(surveyQuestion.getAnswerList().stream().map(Answer::getAnswer).collect(Collectors.toList()))
                .build();
    }
}
