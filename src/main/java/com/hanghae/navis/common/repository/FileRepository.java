package com.hanghae.navis.common.repository;

import com.hanghae.navis.common.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findFileUrlByBoardId(Long boardId);
}
