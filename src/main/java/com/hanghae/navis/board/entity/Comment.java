package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;

import javax.persistence.*;

@Entity(name = "comment")
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Board board;
}
