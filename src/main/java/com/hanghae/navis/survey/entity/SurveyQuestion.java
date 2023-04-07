package com.hanghae.navis.survey.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "surveyQuestion")
@Getter
@NoArgsConstructor
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    //질문
    private String question;

    //질문타입
    private String type;

    //객관식, 체크박스 옵션
    @OneToMany(mappedBy = "surveyQuestion", cascade = {CascadeType.ALL})
    private List<SurveyOption> optionList;

    //응답
    @OneToMany(mappedBy = "surveyQuestion", cascade = {CascadeType.ALL})
    private List<Answer> answerList;

    @ManyToOne
    private Survey survey;

    public SurveyQuestion(Long number, String question, String type, Survey survey) {
        this.number = number;
        this.question = question;
        this.type = type;
        this.survey = survey;
    }
}
