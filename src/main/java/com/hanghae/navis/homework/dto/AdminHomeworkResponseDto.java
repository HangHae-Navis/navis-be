package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@SuperBuilder
public class AdminHomeworkResponseDto extends BoardResponseDto {
    private LocalDateTime expirationTime;
    private Boolean isExpiration;
    private List<FileResponseDto> boardFileList;
    private List<NotSubmitMemberResponseDto> notSubmitMember;
    private List<SubmitMemberResponseDto> submitMember;
    private List<RecentlyViewedDto> recentlyViewed;

    public static AdminHomeworkResponseDto of(Homework homework, List<String> hashtagList, List<FileResponseDto> fileList, List<NotSubmitMemberResponseDto> notSubmitMember, List<SubmitMemberResponseDto> submitMember, GroupMemberRoleEnum role, List<RecentlyViewedDto> rv, GroupMemberRoleEnum authorRole, boolean isAuthor) {
        return AdminHomeworkResponseDto.builder()
                .id(homework.getId())
                .role(role)
                .nickname(homework.getUser().getNickname())
                .title(homework.getTitle())
                .subtitle(homework.getSubtitle())
                .important(homework.getImportant())
                .content(homework.getContent())
                .createAt(homework.getCreatedAt())
                .expirationTime(homework.getExpirationDate())
                .isExpiration(homework.isForce_expiration())
                .hashtagList(hashtagList)
                .boardFileList(fileList)
                .notSubmitMember(notSubmitMember)
                .submitMember(submitMember)
                .recentlyViewed(rv)
                .authorRole(authorRole)
                .isAuthor(isAuthor)
                .build();
    }
}
