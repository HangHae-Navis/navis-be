package com.hanghae.navis.survey.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.survey.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SurveyResponseDto extends BoardResponseDto {
    private boolean submit;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private List<QuestionResponseDto> questionResponseDto;
    private List<RecentlyViewedDto> recentlyViewed;



    public static SurveyResponseDto of(Survey survey, List<QuestionResponseDto> questionResponseDto, List<RecentlyViewedDto> rv, GroupMemberRoleEnum role, boolean submit, List<String> hashtagList, GroupMemberRoleEnum authorRole, boolean isAuthor) {
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
                .hashtagList(hashtagList)
                .recentlyViewed(rv)
                .authorRole(authorRole)
                .isAuthor(isAuthor)
                .build();
    }

    public static SurveyResponseDto submitTrueOf(Survey survey, List<QuestionResponseDto> questionResponseDto, List<RecentlyViewedDto> rv, GroupMemberRoleEnum role, boolean submit, List<String> hashtagList, GroupMemberRoleEnum authorRole, boolean isAuthor) {
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
                .hashtagList(hashtagList)
                .recentlyViewed(rv)
                .authorRole(authorRole)
                .isAuthor(isAuthor)
                .build();
    }

    public static SurveyResponseDto detailOf(Survey survey, List<QuestionResponseDto> questionResponseDto, GroupMemberRoleEnum role, boolean submit, List<String> hashtagList, GroupMemberRoleEnum authorRole, boolean isAuthor) {
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
                .hashtagList(hashtagList)
                .authorRole(authorRole)
                .isAuthor(isAuthor)
                .build();
    }
}
