package com.hanghae.navis.survey.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyRequestDto extends BoardRequestDto {
    private Long expirationDate;
    private List<QuestionRequestDto> questionList;

    public SurveyRequestDto(String title, String hashtagList, Long expirationDate, List<QuestionRequestDto> questionList) {
        super(title, hashtagList);
        this.expirationDate = expirationDate;
        this.questionList = questionList;
    }
}
