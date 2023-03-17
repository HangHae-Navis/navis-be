package com.hanghae.navis.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String profileImage;

    public UserInfoResponseDto(Long id, String username, String nickname, String profileImage) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
