package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserAndGroup(User user, Group group);

    @EntityGraph(attributePaths = {"group", "user"})
    Page<GroupMember> findAllByUser(User user, Pageable pageable);

    Long countByUser(User user);

    Page<GroupMember> findAllByUserAndGroupRole(User user, GroupMemberRoleEnum groupRole, Pageable pageable);
    Page<GroupMember> findAllByUserAndGroupRoleIsNot(User user, GroupMemberRoleEnum groupRole, Pageable pageable);
    List<GroupMember> findAllByGroupIdAndGroupRole(Long groupId, GroupMemberRoleEnum groupRole);
    List<GroupMember> findAllByGroupId(Long groupId);
}
