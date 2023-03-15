package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.dto.BoardFileRequestDto;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BasicBoard {
    public Board(BoardRequestDto requestDto, User user, Group group) {
        super(requestDto, user, group);
    }
}
