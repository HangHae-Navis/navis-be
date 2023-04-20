package com.hanghae.navis.homework.dto;

import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class HomeworkListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private Long important;

    private LocalDateTime createAt;

    private LocalDateTime expirationDate;

    private List<String> hashtagList;

    public static HomeworkListResponseDto of(Homework homework) {
        return HomeworkListResponseDto.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .nickName(homework.getUser().getNickname())
                .content(homework.getContent())
                .important(homework.getImportant())
                .subtitle(homework.getSubtitle())
                .createAt(homework.getCreatedAt())
                .expirationDate(homework.getExpirationDate())
                .hashtagList(homework.getHashtagList().stream()
                        .map(Hashtag::getHashtagName)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Page<HomeworkListResponseDto> toDto(Page<Homework> homeworkPage) {
        return homeworkPage.map(HomeworkListResponseDto::of);
    }
}
