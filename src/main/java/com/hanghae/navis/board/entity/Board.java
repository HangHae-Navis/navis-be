package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BasicBoard {
    public Board(BoardRequestDto requestDto, User user, Group group) {
        super(requestDto, user, group);
        this.getHashtagList();
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.subtitle = requestDto.getSubtitle();
        this.important = requestDto.getImportant();
    }
}
