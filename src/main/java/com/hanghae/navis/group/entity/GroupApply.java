package com.hanghae.navis.group.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class GroupApply extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    private boolean handled;


    public GroupApply(Group group, User user) {
        this.group = group;
        this.user = user;
    }

}
