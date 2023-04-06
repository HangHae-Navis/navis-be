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

    private String question;

    @Enumerated(value = EnumType.STRING)
    private QuestionTypeEnum type;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "surveyQuestion", cascade = {CascadeType.ALL})
    private List<Answer> answerList;
}
