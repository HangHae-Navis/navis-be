package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.BasicBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    public BoardListResponseDto(BasicBoard board) {
        this.id = board.getId();
        this.subtitle = board.getSubtitle();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickName = board.getUser().getNickname();
        this.groupName = board.getGroup().getGroupName();
        this.createAt = board.getCreatedAt();
    }
}
