package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageBasicBoardDto {
    private Long id;
    private String title;
    private String subtitle;
    private String dtype;
    private String nickname;
    private Long important;
    private List<HashtagResponseDto> hashtagResponseDtos;
    private LocalDateTime createdAt;

    public static MainPageBasicBoardDto of(BasicBoard basicBoard) {
        return MainPageBasicBoardDto.builder()
                .id(basicBoard.getId())
                .title(basicBoard.getTitle())
                .subtitle(basicBoard.getSubtitle())
                .dtype(basicBoard.getDtype())
                .nickname(basicBoard.getUser().getNickname())
                .important(basicBoard.getImportant())
                .hashtagResponseDtos(HashtagResponseDto.toDtoList(basicBoard.getHashtagList()))
                .createdAt(basicBoard.getCreatedAt())
                .build();
    }

    public static Page<MainPageBasicBoardDto> toDtoPage(Page<BasicBoard> basicBoardPage) {
        return basicBoardPage.map(MainPageBasicBoardDto::of);
    }
}
