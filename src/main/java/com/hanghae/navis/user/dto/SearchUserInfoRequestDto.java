package com.hanghae.navis.user.dto;

import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchUserInfoRequestDto {
    private String username;
}
