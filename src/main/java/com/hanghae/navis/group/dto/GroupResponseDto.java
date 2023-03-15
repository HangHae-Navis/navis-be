package com.hanghae.navis.group.dto;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {

    private Long groupId;
    private String groupName;
    private String groupInfo;
    private String adminName;
    private int memberNumber;

    public static GroupResponseDto of(Group group) {
        return GroupResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .adminName(group.getUser().getNickname())
                .memberNumber(group.getGroupMember().size())
                .build();
    }

    public static Page<GroupResponseDto> toDtoPage(Page<GroupMember> groupMemberPage) {
        return groupMemberPage.map(m -> GroupResponseDto.of(m.getGroup()));
    }

}
