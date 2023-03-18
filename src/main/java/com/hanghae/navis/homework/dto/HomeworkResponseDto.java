package com.hanghae.navis.homework.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HomeworkResponseDto extends BasicBoardResponseDto {
    private LocalDateTime expirationTime;
    private boolean isExpiration;

    public static HomeworkResponseDto of(Homework homework, List<FileResponseDto> fileList, List<HashtagResponseDto> hashtagList, boolean expiration, LocalDateTime expirationTime) {
        return HomeworkResponseDto.builder()
                .id(homework.getId())
                .nickname(homework.getUser().getNickname())
                .fileList(fileList)
                .title(homework.getTitle())
                .content(homework.getContent())
                .subtitle(homework.getSubtitle())
                .important(homework.getImportant())
                .createAt(homework.getCreatedAt())
                .hashtagList(hashtagList)
                .isExpiration(expiration)
                .expirationTime(expirationTime)
                .build();
    }
}
