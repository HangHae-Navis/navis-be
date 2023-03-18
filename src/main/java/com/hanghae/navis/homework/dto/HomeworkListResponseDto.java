package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardListResponseDto;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@SuperBuilder
public class HomeworkListResponseDto extends BoardListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;
    private Long important;

    private String groupName;

    private LocalDateTime createAt;

    private LocalDateTime expirationDate;

    private List<HashtagResponseDto> hashtagResponseDtoList;

    public static HomeworkListResponseDto of(Homework homework) {
        return HomeworkListResponseDto.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .title(homework.getTitle())
                .content(homework.getContent())
                .important(homework.getImportant())
                .subtitle(homework.getSubtitle())
                .createAt(homework.getCreatedAt())
                .expirationDate(homework.getExpirationDate())
                .hashtagResponseDtoList(HashtagResponseDto.toDtoList(homework.getHashtagList()))
                .build();
    }

    public static Page<HomeworkListResponseDto> toDto(Page<Homework> homeworkPage) {
        return homeworkPage.map(HomeworkListResponseDto::of);
    }
}
