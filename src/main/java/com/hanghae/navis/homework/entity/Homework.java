package com.hanghae.navis.homework.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "homework")
@Getter
@NoArgsConstructor
public class Homework extends BasicBoard {
    private LocalDateTime expirationDate;

    private boolean force_expiration;

    public Homework(HomeworkRequestDto requestDto, User user, Group group, LocalDateTime expirationDate, boolean force_expiration) {
        super(requestDto, user, group);
        this.expirationDate = expirationDate;
        this.force_expiration = force_expiration;
    }
}
