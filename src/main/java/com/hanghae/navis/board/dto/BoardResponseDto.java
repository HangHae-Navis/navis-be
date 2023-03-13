package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.BoardFile;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;

    private String nickname;

    private List<BoardFile> fileList;

    private String content;

    private String groupName;

    private LocalDateTime createAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.groupName = board.getGroup().getGroupName();
        this.nickname = board.getUser().getNickname();
        this.fileList = board.getFileList();
        this.content = board.getContent();
        this.createAt = board.getCreatedAt();
    }
}
