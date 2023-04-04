package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "homework")
@Getter
@NoArgsConstructor
public class Homework extends BasicBoard {
    private LocalDateTime expirationDate;

    private boolean force_expiration;

    @OneToMany(mappedBy = "homework", cascade = {CascadeType.ALL})
    private List<HomeworkSubject> subjectList = new ArrayList<>();

    public Homework(HomeworkRequestDto requestDto, User user, Group group, LocalDateTime expirationDate, boolean force_expiration) {
        super(requestDto, user, group);
        this.expirationDate = expirationDate;
        this.force_expiration = force_expiration;
    }

    public void update(HomeworkRequestDto requestDto, LocalDateTime expirationDate, boolean force_expiration) {
        this.title = requestDto.getTitle();
        this.subtitle = requestDto.getSubtitle();
        this.content = requestDto.getContent();
        this.expirationDate = expirationDate;
        this.force_expiration = force_expiration;
    }
}
