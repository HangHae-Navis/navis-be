package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardListResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeworkListResponseDto extends BoardListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    private LocalDateTime expirationDate;

    public static HomeworkListResponseDto of(Homework homework) {
        return HomeworkListResponseDto.builder()
            .id(homework.getId())
            .title(homework.getTitle())
            .title(homework.getTitle())
            .content(homework.getContent())
            .subtitle(homework.getSubtitle())
            .createAt(homework.getCreatedAt())
            .expirationDate(homework.getExpirationDate())
            .build();
    }
}
