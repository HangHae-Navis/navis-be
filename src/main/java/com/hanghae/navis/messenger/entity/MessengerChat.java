package com.hanghae.navis.messenger.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "messenger_chat")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessengerChat extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean read;
    @ManyToOne
    private Messenger messenger;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    public MessengerChat(String message, boolean read, User author, Messenger messenger) {
        this.message = message;
        this.read = read;
        this.author = author;
        this.messenger = messenger;
    }
}
