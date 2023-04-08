package com.hanghae.navis.survey.entity;

import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "answer")
@Getter
@NoArgsConstructor
public class Answer {
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

    public Answer(String answer, User user, SurveyQuestion surveyQuestion, Survey survey) {
        this.answer = answer;
        this.user = user;
        this.surveyQuestion = surveyQuestion;
        this.survey = survey;
    }
}
