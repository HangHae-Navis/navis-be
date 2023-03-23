package com.hanghae.navis.common.repository.querydsl;

import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.common.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QComment comment = QComment.comment;

    @Override
    public List<Comment> findAllByBasicBoardId(Long boardId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.basicBoard.commentList, comment)
                .fetch();
    }
}
