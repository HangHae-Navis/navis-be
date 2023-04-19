package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.BannedGroupMember;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedGroupMemberRepository extends JpaRepository<BannedGroupMember, Long> {
    Optional<BannedGroupMember> findByUserAndGroup(User user, Group group);
}
