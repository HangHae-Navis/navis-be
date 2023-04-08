package com.hanghae.navis.survey.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.survey.dto.SurveyRequestDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "survey")
@Getter
@NoArgsConstructor
public class Survey extends BasicBoard {
    private LocalDateTime expirationDate;

    private boolean forceExpiration;

    @OneToMany(mappedBy = "survey", cascade = {CascadeType.ALL})
    private List<SurveyQuestion> questionList;

    @OneToMany(mappedBy = "survey", cascade = {CascadeType.ALL})
    private List<Answer> answerList;

    public Survey(SurveyRequestDto requestDto, User user, Group group, LocalDateTime expirationDate, boolean forceExpiration) {
        super(requestDto, user, group);
        this.expirationDate = expirationDate;
        this.forceExpiration = forceExpiration;
    }

    public void forceExpiration() {
        forceExpiration = true;
    }
}