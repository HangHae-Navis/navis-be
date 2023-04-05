package com.hanghae.navis.comment.repository;

import com.hanghae.navis.comment.dto.CommentResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQuerydslRepository implements CommentRepositoryCustom{

    @Override
    public List<CommentResponseDto> findAllByBasicBoardIdOrderByCreatedAt(Long boardId) {
        return null;
    }
}
