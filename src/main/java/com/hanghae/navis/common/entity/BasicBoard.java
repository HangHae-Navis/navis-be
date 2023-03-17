package com.hanghae.navis.common.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.homework.dto.HomeworkResponseDto;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "basic_board")
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn()
public abstract class BasicBoard extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable = false)
    protected String title;
    @Column(nullable = true)
    protected String subtitle;

    @Column(nullable = false)
    protected String content;

    @ManyToOne
    protected User user;

    @ManyToOne
    protected Group group;

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<File> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<Hashtag> hashtagList = new ArrayList<>();

    public BasicBoard(BoardRequestDto requestDto, User user, Group group) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.subtitle = requestDto.getSubtitle();
        this.user = user;
        this.group = group;
    }

    public void update(BoardUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.subtitle = requestDto.getSubtitle();
    }

    public void addFile(File boardFile) {
        fileList.add(boardFile);
    }
}
