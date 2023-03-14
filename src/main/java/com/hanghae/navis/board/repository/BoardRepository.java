package com.hanghae.navis.board.repository;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();
}
