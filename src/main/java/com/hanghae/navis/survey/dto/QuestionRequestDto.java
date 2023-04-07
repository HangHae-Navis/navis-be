package com.hanghae.navis.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionRequestDto {
    private String type;
    private String question;
    private List<String> options;

    public QuestionRequestDto(String type, String question, List<String> options) {
        this.type = type;
        this.question = question;
        this.options = options;
    }
}
