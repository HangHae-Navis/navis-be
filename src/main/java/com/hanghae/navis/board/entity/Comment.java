package com.hanghae.navis.board.entity;

import com.hanghae.navis.comment.dto.CommentRequestDto;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "comment")
@Getter
@NoArgsConstructor
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

    public Comment(CommentRequestDto requestDto, User user, Board board) {
        this.content = requestDto.getContent();
        this.user = user;
        this.board = board;
    }

    public void updateComment(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
