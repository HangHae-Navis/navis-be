package com.hanghae.navis.user.dto;

import com.hanghae.navis.user.entity.User;
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
public class SearchUserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String profileImage;

    public static SearchUserInfoResponseDto of(User user) {
        return SearchUserInfoResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
