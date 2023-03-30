package com.hanghae.navis.homework.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
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
    private GroupMemberRoleEnum role;
    private String nickname;
    private String title;
    private String subtitle;
    private Long important;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime expirationTime;
    private Boolean isExpiration;
    private List<FileResponseDto> boardFileList;
    private List<NotSubmitMemberResponseDto> notSubmitMember;
    private List<SubmitMemberResponseDto> submitMember;

    public static AdminHomeworkResponseDto of(Homework homework, List<FileResponseDto> fileList, List<NotSubmitMemberResponseDto> notSubmitMember, List<SubmitMemberResponseDto> submitMember, GroupMemberRoleEnum role) {
        return AdminHomeworkResponseDto.builder()
                .id(homework.getId())
                .role(role)
                .nickname(homework.getUser().getNickname())
                .title(homework.getTitle())
                .subtitle(homework.getSubtitle())
                .important(homework.getImportant())
                .content(homework.getContent())
                .createdAt(homework.getCreatedAt())
                .expirationTime(homework.getExpirationDate())
                .isExpiration(homework.isForce_expiration())
                .boardFileList(fileList)
                .notSubmitMember(notSubmitMember)
                .submitMember(submitMember)
                .build();
    }
}
