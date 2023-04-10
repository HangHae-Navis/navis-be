package com.hanghae.navis.survey.dto;

import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.survey.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdminSurveyResponseDto {
    private long id;
    private GroupMemberRoleEnum role;
    private String nickname;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private List<AdminSurveyGetDto> answerList;

    public static AdminSurveyResponseDto of(Survey survey, GroupMemberRoleEnum role, List<AdminSurveyGetDto> answerList) {
        return AdminSurveyResponseDto.builder()
                .id(survey.getId())
                .role(role)
                .nickname(survey.getUser().getNickname())
                .title(survey.getTitle())
                .createAt(survey.getCreatedAt())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .answerList(answerList)
                .build();
    }
}
