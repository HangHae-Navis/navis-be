package com.hanghae.navis.messenger.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "messenger")
@Getter
@NoArgsConstructor
public class Messenger extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user1;
    @ManyToOne
    private User user2;

    @ManyToOne
    private Group group;

    private String message;

    private boolean read;
}
