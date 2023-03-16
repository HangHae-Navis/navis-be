package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;

    private String nickname;

    private List<FileResponseDto> fileList;
    private String title;

    private String content;

    private String subtitle;

    private String groupName;

    private LocalDateTime createAt;

    public BoardResponseDto(BasicBoard board, List<FileResponseDto> fileList) {
        this.id = board.getId();
        this.nickname = board.getUser().getNickname();
        this.content = board.getContent();
        this.fileList = fileList;
        this.title = board.getTitle();
        this.subtitle = board.getSubtitle();
        this.groupName = board.getGroup().getGroupName();
        this.createAt = board.getCreatedAt();
    }
}
