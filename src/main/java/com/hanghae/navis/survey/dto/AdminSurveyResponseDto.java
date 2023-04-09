package com.hanghae.navis.survey.dto;

import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.survey.entity.Survey;
import lombok.Builder;
import lombok.Getter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminSurveyResponseDto {
    private Long id;
    private GroupMemberRoleEnum roleEnum;
    private String nickname;
    private LocalDateTime createAt;
    private LocalDateTime expirationDate;
    private boolean forceExpiration;
    private String title;
    private List<QuestionResponseDto> questionResponseDto;
    private List<RecentlyViewedDto> recentlyViewed;
}
