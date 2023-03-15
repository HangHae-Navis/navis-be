package com.hanghae.navis.comment.repository;

import com.hanghae.navis.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
