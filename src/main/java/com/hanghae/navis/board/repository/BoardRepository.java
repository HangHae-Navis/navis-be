package com.hanghae.navis.board.repository;

import com.hanghae.navis.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();
    List<Board> findAllByIdOrderByCreatedAtDesc(Long groupId);

    List<Board> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
}
