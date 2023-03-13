package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupApplyRepository extends JpaRepository<GroupApply, Long> {

//    Optional<GroupApply>
}
