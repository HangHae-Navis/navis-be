package com.hanghae.navis.vote.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "vote")
@Getter
@NoArgsConstructor
public class Vote extends BasicBoard {
    public Vote(BoardRequestDto requestDto, User user, Group group) {
        super(requestDto, user, group);
    }
    @OneToMany(mappedBy = "vote")
    private List<VoteOption> voteOptionList;
}
