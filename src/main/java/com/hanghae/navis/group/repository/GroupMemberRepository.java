package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);
    Long countBy();
}