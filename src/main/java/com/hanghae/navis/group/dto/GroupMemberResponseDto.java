package com.hanghae.navis.group.dto;


import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponseDto {
    private String nickname;
    private LocalDateTime joinedAt;
    private GroupMemberRoleEnum groupMemberRoleEnum;

    public static GroupMemberResponseDto of(GroupMember groupMember) {
        return GroupMemberResponseDto.builder()
                .nickname(groupMember.getUser().getNickname())
                .joinedAt(groupMember.getCreatedAt())
                .groupMemberRoleEnum(groupMember.getGroupRole())
                .build();
    }
}
