package com.hanghae.navis.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;

    public UserInfoResponseDto(Long id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }
}
