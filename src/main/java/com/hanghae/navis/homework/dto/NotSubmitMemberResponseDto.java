package com.hanghae.navis.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotSubmitMemberResponseDto {
    private Long id;
    private String nickname;
    private boolean submit;
    private boolean submitCheck;

    public static NotSubmitMemberResponseDto of(HomeworkSubmitListResponseDto responseDto) {
        return NotSubmitMemberResponseDto.builder()
                .id(responseDto.getUserId())
                .nickname(responseDto.getNickname())
                .submit(false)
                .submitCheck(false)
                .build();
    }
}
