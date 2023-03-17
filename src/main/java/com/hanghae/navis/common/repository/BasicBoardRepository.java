package com.hanghae.navis.common.repository;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicBoardRepository extends JpaRepository<BasicBoard, Long> {
    Page<BasicBoard> findAllByGroupOrderByCreatedAtDesc(Group group, Pageable pageable);
}
