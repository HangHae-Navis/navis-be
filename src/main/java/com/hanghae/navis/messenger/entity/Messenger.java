package com.hanghae.navis.messenger.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.vote.entity.VoteOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "messenger", cascade = {CascadeType.ALL})
    private List<MessengerChat> messengerChatList;
    public Messenger(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
