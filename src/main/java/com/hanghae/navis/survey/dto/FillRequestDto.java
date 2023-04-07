package com.hanghae.navis.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FillRequestDto {
    List<String> answerList;

    public FillRequestDto(List<String> answerList) {
        this.answerList = answerList;
    }
}
