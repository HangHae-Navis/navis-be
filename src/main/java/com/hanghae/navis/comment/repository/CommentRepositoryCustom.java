package com.hanghae.navis.comment.repository;

import com.hanghae.navis.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findAllByBasicBoardIdOrderByCreatedAt(Long boardId);
}