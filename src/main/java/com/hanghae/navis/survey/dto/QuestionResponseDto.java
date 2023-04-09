package com.hanghae.navis.survey.dto;

import com.hanghae.navis.survey.entity.Answer;
import com.hanghae.navis.survey.entity.SurveyOption;
import com.hanghae.navis.survey.entity.SurveyQuestion;
import com.mysema.commons.lang.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
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
    private List<String> createOptionList;
    private List<HashMap<String, String>> optionsList;
    private List<String> answerList;
    private Long size;

    public static QuestionResponseDto of(SurveyQuestion surveyQuestion, List<String> optionList) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .createOptionList(optionList)
                .build();
    }

    public static QuestionResponseDto getOf(SurveyQuestion surveyQuestion) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionsList(surveyQuestion.getOptionList().stream()
                        .map(q -> {
                            HashMap<String, String> newOptionMap = new HashMap<>();
                            newOptionMap.put("optionId", String.valueOf(q.getId()));
                            newOptionMap.put("optionName", String.valueOf(q.getOption()));
                            return newOptionMap;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    public static QuestionResponseDto submitTrueOf(SurveyQuestion surveyQuestion) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionsList(surveyQuestion.getOptionList().stream()
                        .map(q -> {
                            HashMap<String, String> newOptionMap = new HashMap<>();
                            newOptionMap.put("optionId", String.valueOf(q.getId()));
                            newOptionMap.put("optionName", String.valueOf(q.getOption()));
                            return newOptionMap;
                        })
                        .collect(Collectors.toList()))
                .answerList(surveyQuestion.getAnswerList().stream().map(Answer::getAnswer).collect(Collectors.toList()))
                .build();
    }

    public static QuestionResponseDto adminOf(SurveyQuestion surveyQuestion) {
        return QuestionResponseDto.builder()
                .id(surveyQuestion.getId())
                .type(surveyQuestion.getType())
                .number(surveyQuestion.getNumber())
                .question(surveyQuestion.getQuestion())
                .optionsList(surveyQuestion.getOptionList().stream()
                        .map(q -> {
                            HashMap<String, String> newOptionMap = new HashMap<>();
                            newOptionMap.put("optionId", String.valueOf(q.getId()));
                            newOptionMap.put("optionName", String.valueOf(q.getOption()));
                            return newOptionMap;
                        })
                        .collect(Collectors.toList()))
                .answerList(surveyQuestion.getAnswerList().stream().map(Answer::getAnswer).collect(Collectors.toList()))
                .size(surveyQuestion.getAnswerList() == null ? 0L : (long) surveyQuestion.getAnswerList().size())
                .build();
    }
}
