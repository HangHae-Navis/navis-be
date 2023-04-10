package com.hanghae.navis.survey.dto;

public interface AdminSurveyGetDto {
    Long getBoardId();
    String getType();
    Long getQuestionId();
    Long getQuestionNumber();
    String getQuestion();
    String getOptions();
    String getAnswerCount();
}
