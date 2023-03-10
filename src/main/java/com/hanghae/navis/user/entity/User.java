package com.hanghae.navis.user.entity;

import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.UserGroupList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    List<Group> groupList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserGroupList> userGroupList = new ArrayList<>();

    public User(String username,String nickname, String password, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

}