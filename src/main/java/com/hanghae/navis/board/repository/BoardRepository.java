package com.hanghae.navis.board.repository;

import com.hanghae.navis.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
    Page<Board> findAllByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);
}
