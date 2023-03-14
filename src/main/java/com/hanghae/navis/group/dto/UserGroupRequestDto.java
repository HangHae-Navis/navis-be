package com.hanghae.navis.group.dto;


import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;

@Getter
public class UserGroupRequestDto {
    private User user;
    private Group group;
}
