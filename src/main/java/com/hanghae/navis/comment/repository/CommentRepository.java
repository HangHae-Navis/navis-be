package com.hanghae.navis.comment.repository;

import com.hanghae.navis.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBasicBoardIdOrderByCreatedAt(Long boardId);
    Page<Comment> findAllByBasicBoardIdOrderByCreatedAt(Long boardId, Pageable pageable);
}
