package com.hanghae.navis.vote.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.vote.dto.VoteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "vote")
@Getter
@NoArgsConstructor
public class Vote extends BasicBoard {

    private LocalDateTime expirationDate;
    private boolean force_expiration;
    @OneToMany(mappedBy = "vote")
    private List<VoteOption> voteOptionList;

    public Vote(VoteRequestDto requestDto, User user, Group group, LocalDateTime expirationDate, boolean forceExpiration) {
        super(requestDto, user, group);
        this.expirationDate = expirationDate;
        this.force_expiration = forceExpiration;
    }

}
