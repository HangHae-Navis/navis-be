package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.BoardFile;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;

    private String nickname;

    private List<BoardFile> fileList;

    private String content;

    private String subtitle;

    private String groupName;

    private LocalDateTime createAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.nickname = board.getUser().getNickname();
        this.fileList = board.getFileList();
        this.content = board.getContent();
        this.subtitle = board.getSubtitle();
        this.groupName = board.getGroup().getGroupName();
        this.createAt = board.getCreatedAt();
    }
}
