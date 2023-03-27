package com.hanghae.navis.vote.dto;

import com.hanghae.navis.user.entity.User;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickUserInfoDto {
    Long id;
    String nickname;
    String username;
    String profileImage;
    Long voteOptionId;

    public static PickUserInfoDto of(User user, Long voteOptionId) {
        return PickUserInfoDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .voteOptionId(voteOptionId)
                .build();
    }
}
