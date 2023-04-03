package com.hanghae.navis.homework.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.entity.HomeworkSubject;
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
    private boolean submit;
    private SubmitResponseDto submitResponseDto;

    public static HomeworkResponseDto of(Homework homework, List<FileResponseDto> fileList, List<String> hashtagList, boolean expiration, LocalDateTime expirationTime, GroupMemberRoleEnum role) {
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
                .submit(false)
                .role(role)
                .build();
    }

    public static HomeworkResponseDto of(Homework homework, List<FileResponseDto> fileList, List<String> hashtagList, boolean expiration, LocalDateTime expirationTime, GroupMemberRoleEnum role, SubmitResponseDto submitResponseDto) {
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
                .role(role)
                .submitResponseDto(submitResponseDto)
                .build();
    }
}
