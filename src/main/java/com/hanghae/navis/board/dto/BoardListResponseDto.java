package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardListResponseDto {
    private Long id;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    public BoardListResponseDto(Board board) {
        this.id = board.getId();
        this.subtitle = board.getSubtitle();
        this.content = board.getContent();
        this.nickName = board.getUser().getNickname();
        this.groupName = board.getGroup().getGroupName();
        this.createAt = board.getCreatedAt();
    }
}
