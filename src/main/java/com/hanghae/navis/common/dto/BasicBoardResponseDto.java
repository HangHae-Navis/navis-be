package com.hanghae.navis.common.dto;

import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.vote.dto.VoteListResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BasicBoardResponseDto {
    private Long id;
    private String nickname;
    private List<FileResponseDto> fileList;
    private String title;
    private String content;
    private String subtitle;
    private LocalDateTime createAt;
}
