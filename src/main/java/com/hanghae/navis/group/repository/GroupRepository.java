package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {


    Optional<Group> findByGroupCode(String groupCode);
}
