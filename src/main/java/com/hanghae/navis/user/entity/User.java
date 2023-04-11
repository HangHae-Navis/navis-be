package com.hanghae.navis.user.entity;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.survey.entity.Answer;
import com.hanghae.navis.vote.entity.Vote;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Group> groupList = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    List<GroupMember> groupMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<BasicBoard> basicBoardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Homework> homeworkList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Vote> voteList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Answer> answerList = new ArrayList<>();



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

    public User updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
    public User updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }
    public User updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public User UpdatePassword(String password) {
        this.password = password;
        return this;
    }

}