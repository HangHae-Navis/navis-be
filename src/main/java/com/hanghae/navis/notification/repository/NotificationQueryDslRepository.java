package com.hanghae.navis.notification.repository;

import com.hanghae.navis.messenger.entity.Messenger;
import com.hanghae.navis.messenger.entity.QMessenger;
import com.hanghae.navis.messenger.entity.QMessengerChat;
import com.hanghae.navis.notification.entity.QNotification;
import com.hanghae.navis.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NotificationQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public NotificationQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public void updateIsReadByUserId(Long userId) {
        QNotification notification = QNotification.notification;

        jpaQueryFactory.update(notification)
                .set(notification.isRead, true)
                .where(notification.user.id.eq(userId))
                .execute();
    }
}
