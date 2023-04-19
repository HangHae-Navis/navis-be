package com.hanghae.navis.survey.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "surveyOption", cascade = {CascadeType.ALL})
    private List<Answer> answerList;

    public SurveyOption(String option, SurveyQuestion surveyQuestion) {
        this.option = option;
        this.surveyQuestion = surveyQuestion;
    }
}
