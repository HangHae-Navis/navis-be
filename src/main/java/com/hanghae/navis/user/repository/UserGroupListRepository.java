package com.hanghae.navis.user.repository;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.UserGroupList;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupListRepository extends JpaRepository<UserGroupList, Long> {
    Optional<UserGroupList> findByUserAndGroup(User user, Group group);
}
