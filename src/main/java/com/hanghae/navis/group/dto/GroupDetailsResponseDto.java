package com.hanghae.navis.group.dto;

import com.hanghae.navis.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailsResponseDto {
    private String groupName;
    private String groupInfo;
    private String groupImage;
    private String groupCode;
    private List<GroupMemberResponseDto> groupMembers;

    public static GroupDetailsResponseDto of(Group group) {

        return GroupDetailsResponseDto.builder()
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .groupImage(group.getGroupImage())
                .groupCode(group.getGroupCode())
                .groupMembers(group.getGroupMember().stream().map(GroupMemberResponseDto::of).collect(Collectors.toList()))
                .build();
    }
}
