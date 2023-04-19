package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.homework.entity.Homework;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageBasicBoardDto {
    private Long id;
    private String title;
    private String subtitle;
    private String dtype;
    private String nickname;
    private Long important;
    private List<String> hashtagList;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;

    public static MainPageBasicBoardDto of(BasicBoard basicBoard) {
        return MainPageBasicBoardDto.builder()
                .id(basicBoard.getId())
                .title(basicBoard.getTitle())
                .subtitle(basicBoard.getSubtitle())
                .dtype(basicBoard.getDtype())
                .nickname(basicBoard.getUser().getNickname())
                .important(basicBoard.getImportant())
                .hashtagList(basicBoard.getHashtagList().stream()
                        .map(Hashtag::getHashtagName)
                        .collect(Collectors.toList()))
                .createdAt(basicBoard.getCreatedAt())
                .build();
    }

    public static Page<MainPageBasicBoardDto> toDtoPage(Page<BasicBoard> basicBoardPage) {
        return basicBoardPage.map(MainPageBasicBoardDto::of);
    }

}
