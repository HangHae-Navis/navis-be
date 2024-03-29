package com.hanghae.navis.group.repository;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.common.entity.QBasicBoard;
import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.*;
import com.hanghae.navis.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public QueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<GroupResponseDto> findAllGroupByUserId(Long userId, String category, Pageable pageable) {
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(gm.count())
                .from(gm)
                .where(gm.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<GroupResponseDto>(responseDtos, pageable, count);
    }

    public List<RecentlyViewedDto> findRecentlyViewedsByGroupMemeber(Long groupMemberId) {
        QGroupMember gm = QGroupMember.groupMember;
        QBasicBoard bb = QBasicBoard.basicBoard;
        QRecentlyViewed rv = QRecentlyViewed.recentlyViewed;

        List<RecentlyViewedDto> responseDtos = jpaQueryFactory
                .select(Projections.bean(RecentlyViewedDto.class,
                        bb.id.as("id"),
                        bb.title.as("title"),
                        bb.dtype.as("dtype")))
                .distinct()
                .from(rv)
                .leftJoin(bb).on(rv.basicBoard.id.eq(bb.id))
                .leftJoin(gm).on(rv.groupMember.id.eq(gm.id))
                .where(rv.groupMember.id.eq(groupMemberId))
                .orderBy(rv.id.desc())
                .limit(5)
                .fetch();

        return responseDtos;
    }

}
