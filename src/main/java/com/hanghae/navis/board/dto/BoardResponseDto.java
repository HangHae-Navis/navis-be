package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BoardResponseDto extends BasicBoardResponseDto {
    public static BoardResponseDto of(Board board, List<FileResponseDto> fileList, List<HashtagResponseDto> hashtagList) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .nickname(board.getUser().getNickname())
                .fileList(fileList)
                .title(board.getTitle())
                .content(board.getContent())
                .subtitle(board.getSubtitle())
                .createAt(board.getCreatedAt())
                .hashtagList(hashtagList)
                .build();
    }
}
