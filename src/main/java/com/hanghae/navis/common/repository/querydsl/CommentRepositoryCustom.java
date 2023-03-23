package com.hanghae.navis.common.repository.querydsl;

import com.hanghae.navis.common.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByBasicBoardId(Long boardId);
}
