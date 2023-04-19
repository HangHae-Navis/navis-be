package com.hanghae.navis.notice.dto;

import com.hanghae.navis.board.dto.BoardListResponseDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NoticeListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    private List<String> hashtagList;

    public static NoticeListResponseDto of(BasicBoard notice, List<HashtagResponseDto> hashtagList) {
        return NoticeListResponseDto.builder()
                .id(notice.getId())
                .subtitle(notice.getUser().getNickname())
                .title(notice.getTitle())
                .content(notice.getTitle())
                .nickName(notice.getContent())
                .groupName(notice.getSubtitle())
                .createAt(notice.getCreatedAt())
                .hashtagList(notice.getHashtagList().stream()
                        .map(Hashtag::getHashtagName)
                        .collect(Collectors.toList()))
                .build();
    }

}
