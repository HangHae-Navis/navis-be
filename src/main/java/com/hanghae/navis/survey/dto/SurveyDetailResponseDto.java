package com.hanghae.navis.survey.dto;

import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.survey.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SurveyDetailResponseDto {
    private Long id;
    private String title;
    private GroupMemberRoleEnum role;
    private String nickname;
    private LocalDateTime createAt;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private List<QuestionResponseDto> userAnswerList;

    public static SurveyDetailResponseDto of(Survey survey, GroupMemberRoleEnum role, List<QuestionResponseDto> userAnswerList) {
        return SurveyDetailResponseDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .role(role)
                .nickname(survey.getUser().getNickname())
                .createAt(survey.getCreatedAt())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .userAnswerList(userAnswerList)
                .build();
    }
}
