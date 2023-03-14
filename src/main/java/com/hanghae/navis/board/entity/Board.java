package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.dto.BoardFileRequestDto;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.QUserGroupList;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @OneToMany(mappedBy = "board")
    private List<BoardFile> fileList = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();

    public Board(BoardRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void addFile(BoardFile boardFile) {
        fileList.add(boardFile);
    }
}
