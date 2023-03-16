package com.hanghae.navis.vote.repository;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.vote.entity.Vote;
import com.hanghae.navis.vote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
}
