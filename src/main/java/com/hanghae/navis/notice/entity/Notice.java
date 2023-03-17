package com.hanghae.navis.notice.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.notice.dto.NoticeRequestDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity(name = "notice")
@Getter
@NoArgsConstructor
public class Notice extends BasicBoard {
    public Notice(NoticeRequestDto requestDto, User user, Group group) {
        super(requestDto, user, group);
    }
}
