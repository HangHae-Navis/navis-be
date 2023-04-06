package com.hanghae.navis.survey.entity;

import javax.persistence.*;

@Entity(name = "surveyOption")
public class SurveyOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option;

    @ManyToOne
    private SurveyQuestion surveyQuestion;
}
