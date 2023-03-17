package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BoardListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    private List<HashtagResponseDto> hashtagList;

    public static BoardListResponseDto of(BasicBoard board, List<HashtagResponseDto> hashtagList) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .subtitle(board.getUser().getNickname())
                .title(board.getTitle())
                .content(board.getTitle())
                .nickName(board.getContent())
                .groupName(board.getSubtitle())
                .createAt(board.getCreatedAt())
                .hashtagList(hashtagList)
                .build();
    }
}
