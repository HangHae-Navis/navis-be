package com.hanghae.navis.homework.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdminHomeworkResponseDto {
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private LocalDateTime expirationTime;
    private Boolean isExpiration;
    private List<FileResponseDto> boardFileList;
    private List<NotSubmitMemberResponseDto> notSubmitMember;
    private List<SubmitMemberResponseDto> submitMember;

    public static AdminHomeworkResponseDto of(Homework homework, List<FileResponseDto> fileList, List<NotSubmitMemberResponseDto> notSubmitMember, List<SubmitMemberResponseDto> submitMember) {
        return AdminHomeworkResponseDto.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .subtitle(homework.getSubtitle())
                .content(homework.getContent())
                .expirationTime(homework.getExpirationDate())
                .isExpiration(homework.isForce_expiration())
                .boardFileList(fileList)
                .notSubmitMember(notSubmitMember)
                .submitMember(submitMember)
                .build();
    }
}
