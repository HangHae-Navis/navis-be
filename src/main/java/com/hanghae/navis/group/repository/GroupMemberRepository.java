package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserAndGroup(User user, Group group);

    @EntityGraph(attributePaths = {"group", "user"})
    Page<GroupMember> findAllByUser(User user, Pageable pageable);

//    @Query(value = "SELECT `groups`.id as groupId, `groups`.group_name as groupName, `groups`.group_Info as groupInfo, " +
//            "`groups`.group_image, users.nickname as adminName, group_size.group_count as memberNumber FROM group_member " +
//            "LEFT JOIN `groups` ON group_member.group_id = `groups`.id " +
//            "LEFT JOIN users ON group_member.user_id = users.id " +
//            "LEFT JOIN (SELECT group_id, COUNT(user_id) AS group_count FROM group_member GROUP BY group_id) AS group_size " +
//            "ON `groups`.id = group_size.group_id " +
//            "WHERE group_member.user_id = :userId " +
//            "GROUP BY `groups`.id", nativeQuery = true)
//    List<GroupResponseDtoInterface> findAllByUserIdQuery(@Param("userId") Long userId);

    Long countByUser(User user);

    Page<GroupMember> findAllByUserAndGroupRole(User user, GroupMemberRoleEnum groupRole, Pageable pageable);
    Page<GroupMember> findAllByUserAndGroupRoleIsNot(User user, GroupMemberRoleEnum groupRole, Pageable pageable);
    List<GroupMember> findAllByGroupIdAndGroupRole(Long groupId, GroupMemberRoleEnum groupRole);
}
