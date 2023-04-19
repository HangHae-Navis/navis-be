package com.hanghae.navis.common.repository;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicBoardRepository extends JpaRepository<BasicBoard, Long> {

    @EntityGraph(attributePaths = {"hashtagList"})
    Page<BasicBoard> findAllByGroupOrderByCreatedAtDesc(Group group, Pageable pageable);
    @EntityGraph(attributePaths = {"hashtagList"})
    Page<BasicBoard> findAllByGroupOrderByImportantDesc(Group group, Pageable pageable);
    @EntityGraph(attributePaths = {"hashtagList"})
    Page<BasicBoard> findAllByGroupAndDtypeOrderByCreatedAtDesc(Group group, String dtype, Pageable pageable);
    @EntityGraph(attributePaths = {"hashtagList"})
    Page<BasicBoard> findAllByGroupAndDtypeOrderByImportantDesc(Group group, String dtype, Pageable pageable);

    void deleteByUser(User user);
}
