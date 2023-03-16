package com.hanghae.navis.vote.entity;

import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.vote.dto.VoteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "vote")
@Getter
@NoArgsConstructor
public class Vote extends BasicBoard {

    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<VoteOption> voteOptionList;

    public Vote(VoteRequestDto requestDto, User user, Group group, LocalDateTime expirationDate, boolean forceExpiration) {
        super(requestDto, user, group);
        this.expirationDate = expirationDate;
        this.forceExpiration = forceExpiration;
    }
//
//    public void update(VoteRequestDto requestDto, LocalDateTime expirationDate) {
//        this.content = requestDto.getContent();
//        this.subtitle = requestDto.getSubtitle();
//        this.expirationDate = expirationDate;
//    }

    public void forceExpiration(){
        forceExpiration = true;
    }
}
