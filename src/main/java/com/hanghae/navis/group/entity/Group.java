package com.hanghae.navis.group.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "groups")
public class Group extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;
    @Column
    private String groupInfo;

    @Column(nullable = false, unique = true)
    private String groupCode;

    @Column
    private String groupImage;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    List<GroupMember> groupMember = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    List<BannedGroupMember> bannedMember = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Homework> homeworkList = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BasicBoard> basicBoards = new ArrayList<>();

    public Group(String groupName, String groupInfo, User user) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.user = user;
    }

    public Group(GroupRequestDto groupRequestDto, User user) {
        this.groupName = groupRequestDto.getGroupName();
        this.groupInfo = groupRequestDto.getGroupInfo();
        this.user = user;
    }

    public void addGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public void updateGroup(GroupRequestDto groupRequestDto) {
        this.groupName = groupRequestDto.getGroupName();
        this.groupInfo = groupRequestDto.getGroupInfo();
    }
}
