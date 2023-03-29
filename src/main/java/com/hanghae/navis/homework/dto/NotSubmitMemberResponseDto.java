package com.hanghae.navis.homework.dto;

import com.hanghae.navis.group.entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotSubmitMemberResponseDto {
    private Long id;
    private String nickname;
    private Boolean submit;

    public static NotSubmitMemberResponseDto of(HomeworkSubmitListResponseDto responseDto) {
        return NotSubmitMemberResponseDto.builder()
                .id(responseDto.getUserId())
                .nickname(responseDto.getNickname())
                .submit(false)
                .build();
    }
}
