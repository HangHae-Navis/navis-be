package com.hanghae.navis.vote.entity;

import com.hanghae.navis.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "vote")
@Getter
@NoArgsConstructor
public class Vote extends Board {
    @OneToMany(mappedBy = "vote")
    private List<VoteOption> voteOptionList;
}
