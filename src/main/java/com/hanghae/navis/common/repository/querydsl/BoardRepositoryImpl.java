package com.hanghae.navis.common.repository.querydsl;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.QBoard;
import com.hanghae.navis.common.entity.QBasicBoard;
import com.hanghae.navis.common.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QBasicBoard basicBoard = QBasicBoard.basicBoard;
    QBoard board = QBoard.board;
    QComment comment = QComment.comment;

    @Override
    public Board findById(Long boardId) {
        return null;
    }
}
