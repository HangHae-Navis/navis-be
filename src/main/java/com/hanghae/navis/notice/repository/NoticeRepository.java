package com.hanghae.navis.notice.repository;

import com.hanghae.navis.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByGroupIdOrderByCreatedAtDesc(Long groupId);
}
