package com.hanghae.navis.vote.repository;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.vote.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Page<Vote> findAllByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);
}
