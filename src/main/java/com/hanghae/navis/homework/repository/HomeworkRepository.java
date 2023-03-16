package com.hanghae.navis.homework.repository;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
}
