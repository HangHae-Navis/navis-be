package com.hanghae.navis.survey.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "surveyOption")
@Getter
@NoArgsConstructor
public class SurveyOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option;

    @ManyToOne
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    private Answer answer;

    public SurveyOption(String option, SurveyQuestion surveyQuestion) {
        this.option = option;
        this.surveyQuestion = surveyQuestion;
    }
}
