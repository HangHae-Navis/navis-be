package com.hanghae.navis.user.dto;

import com.hanghae.navis.group.dto.GroupDetailsResponseDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.vote.dto.OptionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserInfoResponseDto{
    private Long id;
    private String username;
    private String nickname;
    private String profileImage;

    private LocalDateTime createdAt;
    private List<UserGroupDetailDto> groupInfo;

    public static UserInfoResponseDto of(User user, List<UserGroupDetailDto> groupInfo) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .groupInfo(groupInfo)
                .build();
    }
}
