package com.hanghae.navis.group.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "group_member")
public class GroupMember extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "groupMember")
    private List<RecentlyViewed> recentlyViewedList;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GroupMemberRoleEnum groupRole;

    public GroupMember(User user, Group group) {
        this.user = user;
        this.group = group;
        this.groupRole = GroupMemberRoleEnum.USER;
    }
}

