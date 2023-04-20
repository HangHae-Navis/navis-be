package com.hanghae.navis.group.dto;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.homework.entity.Homework;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {

    private Long groupId;
    private String groupName;
    private String groupInfo;
    private String groupImage;
    private String adminName;
    private int memberNumber;
    private LocalDateTime expirationDate;
    private String homeworkTitle;
    private Long deadlineNumber;

    public static GroupResponseDto of(Group group) {
        return GroupResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .groupImage(group.getGroupImage())
                .adminName(group.getUser().getNickname())
                .memberNumber(group.getGroupMember().size())
                .build();
    }

    public static Page<GroupResponseDto> toDtoPage(Page<GroupMember> groupMemberPage) {
        return groupMemberPage.map(m -> GroupResponseDto.of(m.getGroup()));
    }

    public void addDeadline(Homework homework) {
        this.homeworkTitle = homework.getTitle();
        this.expirationDate = homework.getExpirationDate();
    }

    public void addDeadlineNumber(Long deadlineNumber) {
        this.deadlineNumber = deadlineNumber;
    }

}
