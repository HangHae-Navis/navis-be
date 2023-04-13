package com.hanghae.navis.survey.dto;

import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.survey.entity.Survey;
import com.hanghae.navis.survey.entity.SurveyQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SurveyResponseDto {
    private long id;
    private GroupMemberRoleEnum role;
    private String nickname;
    private boolean submit;
    private Long important;
    private LocalDateTime createAt;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private String title;
    private List<QuestionResponseDto> questionResponseDto;
    private List<RecentlyViewedDto> recentlyViewed;

    public static SurveyResponseDto of(Survey survey, List<QuestionResponseDto> questionResponseDto, List<RecentlyViewedDto> rv, GroupMemberRoleEnum role, boolean submit) {
        return SurveyResponseDto.builder()
                .id(survey.getId())
                .role(role)
                .nickname(survey.getUser().getNickname())
                .submit(submit)
                .important(survey.getImportant())
                .createAt(survey.getCreatedAt())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .title(survey.getTitle())
                .questionResponseDto(questionResponseDto)
                .recentlyViewed(rv)
                .build();
    }

    public static SurveyResponseDto submitTrueOf(Survey survey, List<QuestionResponseDto> questionResponseDto, List<RecentlyViewedDto> rv, GroupMemberRoleEnum role, boolean submit) {
        return SurveyResponseDto.builder()
                .id(survey.getId())
                .role(role)
                .nickname(survey.getUser().getNickname())
                .submit(submit)
                .important(survey.getImportant())
                .createAt(survey.getCreatedAt())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .title(survey.getTitle())
                .questionResponseDto(questionResponseDto)
                .recentlyViewed(rv)
                .build();
    }

    public static SurveyResponseDto detailOf(Survey survey, List<QuestionResponseDto> questionResponseDto, GroupMemberRoleEnum role, boolean submit) {
        return SurveyResponseDto.builder()
                .id(survey.getId())
                .role(role)
                .nickname(survey.getUser().getNickname())
                .submit(submit)
                .important(survey.getImportant())
                .createAt(survey.getCreatedAt())
                .expirationDate(survey.getExpirationDate())
                .forceExpiration(survey.isForceExpiration())
                .title(survey.getTitle())
                .questionResponseDto(questionResponseDto)
                .build();
    }
}
