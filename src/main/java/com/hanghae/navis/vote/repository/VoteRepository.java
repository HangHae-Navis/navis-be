package com.hanghae.navis.vote.repository;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
}
