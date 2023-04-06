package com.hanghae.navis.survey.entity;

import com.hanghae.navis.group.entity.GroupMember;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupmember_id")
    private GroupMember groupMember;

    @ManyToOne
    private SurveyQuestion surveyQuestion;

    public Answer(String answer, GroupMember groupMember, SurveyQuestion surveyQuestion) {
        this.answer = answer;
        this.groupMember = groupMember;
        this.surveyQuestion = surveyQuestion;
    }
}
