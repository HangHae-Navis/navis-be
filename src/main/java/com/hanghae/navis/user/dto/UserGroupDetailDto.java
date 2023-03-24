package com.hanghae.navis.user.dto;

import com.hanghae.navis.group.dto.BannedMemberResponseDto;
import com.hanghae.navis.group.dto.GroupDetailsResponseDto;
import com.hanghae.navis.group.dto.GroupMemberResponseDto;
import com.hanghae.navis.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserGroupDetailDto{
    private String groupName;
    private String groupInfo;
    private String groupImage;
    private String groupCode;
    private LocalDateTime createdAt;
    private int groupMemberCount;
    private int bannedMemberCount;

    public static UserGroupDetailDto of(Group group) {
        return UserGroupDetailDto.builder()
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .groupImage(group.getGroupImage())
                .groupCode(group.getGroupCode())
                .createdAt(group.getCreatedAt())
                .groupMemberCount(group.getGroupMember().size())
                .bannedMemberCount(group.getBannedMember().size())
                .build();
    }
}
