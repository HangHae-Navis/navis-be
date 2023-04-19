package com.hanghae.navis.messenger.repository;

import com.hanghae.navis.messenger.dto.MessengerListResponseDto;
import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.QMessenger;
import com.hanghae.navis.messenger.entity.QMessengerChat;
import com.hanghae.navis.user.entity.QUser;
import com.hanghae.navis.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.NullnessHelper.coalesce;
import static reactor.core.publisher.Mono.when;

@Repository
public class MessengerQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public MessengerQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Optional<Messenger> findByMessenger(User user1, User user2) {
        QMessenger messenger = QMessenger.messenger;

        Messenger result = jpaQueryFactory.selectFrom(messenger)
                .where(messenger.user1.eq(user1).and(messenger.user2.eq(user2))
                        .or(messenger.user1.eq(user2).and(messenger.user2.eq(user1))))
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    public void updateRead(Long roomId, Long userId) {
        QMessengerChat messengerChat = QMessengerChat.messengerChat;
        BooleanExpression isMatched = messengerChat.messenger.id.eq(roomId);
        BooleanExpression isNotAuthor = messengerChat.author.id.ne(userId);

        jpaQueryFactory.update(messengerChat)
                .set(messengerChat.read, true)
                .where(isMatched.and(isNotAuthor))
                .execute();
    }
}
