package com.hanghae.navis.homework.repository;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.homework.entity.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
    Page<Homework> findAllByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    List<Homework> findAllByExpirationDateBetweenAndGroupOrderByExpirationDateAsc(LocalDateTime today, LocalDateTime tomorrow, Group group);
    Optional<Homework> findFirstByExpirationDateBetweenAndGroupOrderByExpirationDateAsc(LocalDateTime today, LocalDateTime tomorrow, Group group);

}
