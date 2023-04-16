package com.hanghae.navis.group.dto;

import com.hanghae.navis.group.entity.BannedGroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannedMemberResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private LocalDateTime bannedAt;

    public static BannedMemberResponseDto of(BannedGroupMember bannedGroupMember) {
        return BannedMemberResponseDto.builder()
                .id(bannedGroupMember.getId())
                .username(bannedGroupMember.getUser().getUsername())
                .nickname(bannedGroupMember.getUser().getNickname())
                .bannedAt(bannedGroupMember.getCreatedAt())
                .build();
    }
}
