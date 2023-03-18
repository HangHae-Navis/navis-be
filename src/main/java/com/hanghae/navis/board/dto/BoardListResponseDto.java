package com.hanghae.navis.board.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long important;

    private LocalDateTime createAt;

    private List<String> hashtagList;

    public static BoardListResponseDto of(BasicBoard board) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .subtitle(board.getUser().getNickname())
                .title(board.getTitle())
                .content(board.getTitle())
                .important(board.getImportant())
                .nickName(board.getContent())
                .groupName(board.getSubtitle())
                .createAt(board.getCreatedAt())
                .hashtagList(board.getHashtagList().stream()
                        .map(Hashtag::getHashtagName)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Page<BoardListResponseDto> toDtoPage(Page<Board> boardPage) {
        return boardPage.map(BoardListResponseDto::of);
    }
}
