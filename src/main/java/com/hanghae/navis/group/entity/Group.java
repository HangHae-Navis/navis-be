package com.hanghae.navis.group.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.meet.entity.MeetRoom;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "groupClass")
public class Group extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false, unique = true)
    private String groupCode;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "group")
    List<UserGroupList> userGroupList = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    List<MeetRoom> meetRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    List<Homework> homeworkList = new ArrayList<>();
}
