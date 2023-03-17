package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicBoardResponseDto {
    private Long id;
    private String title;
    private String subtitle;
    private String nickname;
    private LocalDateTime createdAt;

    public static BasicBoardResponseDto of(BasicBoard basicBoard) {
        return BasicBoardResponseDto.builder()
                .id(basicBoard.getId())
                .title(basicBoard.getTitle())
                .subtitle(basicBoard.getSubtitle())
                .nickname(basicBoard.getUser().getNickname())
                .createdAt(basicBoard.getCreatedAt())
                .build();
    }

    public static Page<BasicBoardResponseDto> toDtoPage(Page<BasicBoard> basicBoardPage) {
        return basicBoardPage.map(BasicBoardResponseDto::of);
    }
}
