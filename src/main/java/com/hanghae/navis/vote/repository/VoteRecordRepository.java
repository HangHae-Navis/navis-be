package com.hanghae.navis.vote.repository;

import com.hanghae.navis.vote.entity.Vote;
import com.hanghae.navis.vote.entity.VoteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {
    Optional<VoteRecord> findByGroupMemberIdAndVoteOptionId(Long groupMemberId, Long voteOptionId);
    void deleteByGroupMemberIdAndVoteId(Long groupMemberId, Long voteOptionId);
}
