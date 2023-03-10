package com.hanghae.navis.board.entity;

import com.hanghae.navis.comment.entity.Comment;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Getter
@NoArgsConstructor
public class Board extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

//    @ManyToOne
//    private Group group;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();
}
