package com.hanghae.navis.user.entity;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.homework.entity.Homework;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;
    @Column
    private String profileImage;

    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    List<Group> groupList = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    List<GroupMember> groupMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Homework> homeworkList = new ArrayList<>();



    public User(String username, String nickname, String password, UserRoleEnum userRoleEnum) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = userRoleEnum;
    }

    public User(String username, String nickname, Long kakaoId, String password, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.password = password;
        this.role = role;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
    public User profileImageUpdate(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }
    public User NicknameUpdate(String nickname) {
        this.nickname = nickname;
        return this;
    }

}