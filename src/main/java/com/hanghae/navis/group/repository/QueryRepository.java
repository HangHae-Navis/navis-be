package com.hanghae.navis.group.repository;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.entity.QGroup;
import com.hanghae.navis.group.entity.QGroupMember;
import com.hanghae.navis.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public QueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<GroupResponseDto> findAllGroupByUserId(Long userId, String category) {
        QGroupMember gm = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        switch (category) {
            case "myOwn" :
                builder.and(gm.groupRole.eq(GroupMemberRoleEnum.ADMIN));
                break;
            case "joined" :
                builder.and(gm.groupRole.ne(GroupMemberRoleEnum.ADMIN));
                break;
            case "all" :
                break;
            default:
                throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
        }


        List<GroupResponseDto> responseDtos =jpaQueryFactory
                .select(Projections.bean(GroupResponseDto.class,
                        group.id.as("groupId"),
                        group.groupName.as("groupName"),
                        group.groupInfo.as("groupInfo"),
                        group.groupImage.as("groupImage"),
                        group.user.nickname.as("adminName"),
                        group.groupMember.size().as("memberNumber")))
                .from(gm)
                .leftJoin(group).on(gm.group.id.eq(group.id))
                .leftJoin(user).on(gm.user.id.eq(user.id))
                .where(gm.user.id.eq(userId), builder)
                .groupBy(group.id)
                .fetch();

        return responseDtos;
    }
}
