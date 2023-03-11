package com.hanghae.navis.user.entity;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.Comment;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.UserGroupList;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.entity.HomeworkComment;
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
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    List<Group> groupList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserGroupList> userGroupList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Homework> homeworkList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<HomeworkComment> homeworkCommentList = new ArrayList<>();


    public User(String username, String nickname, String password, String email, UserRoleEnum userRoleEnum) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = userRoleEnum;
    }

    public User(String username, String nickname, Long kakaoId, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

}