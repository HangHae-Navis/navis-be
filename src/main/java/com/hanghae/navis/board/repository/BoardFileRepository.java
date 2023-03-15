package com.hanghae.navis.board.repository;

import com.hanghae.navis.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findFileUrlByBoardId(Long boardId);
}
