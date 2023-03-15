package com.hanghae.navis.board.repository;

import com.hanghae.navis.board.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<File, Long> {
    List<File> findFileUrlByBoardId(Long boardId);
}
