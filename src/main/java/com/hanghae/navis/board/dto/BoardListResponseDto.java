package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<HashtagResponseDto> hashtagList;

    public BoardListResponseDto(BasicBoard board, List<HashtagResponseDto> hashtagList) {
        this.id = board.getId();
        this.subtitle = board.getSubtitle();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickName = board.getUser().getNickname();
        this.groupName = board.getGroup().getGroupName();
        this.createAt = board.getCreatedAt();
        this.hashtagList = hashtagList;
    }
}
