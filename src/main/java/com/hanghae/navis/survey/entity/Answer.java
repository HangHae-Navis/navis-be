package com.hanghae.navis.survey.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "answer")
@Getter
@NoArgsConstructor
public class Answer extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @ManyToOne
    private User user;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    private SurveyOption surveyOption;

    public Answer(String answer, User user, SurveyQuestion surveyQuestion, Survey survey, SurveyOption surveyOption) {
        this.answer = answer;
        this.user = user;
        this.surveyQuestion = surveyQuestion;
        this.survey = survey;
        this.surveyOption = surveyOption;
    }
}
