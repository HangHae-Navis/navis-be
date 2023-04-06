package com.hanghae.navis.common.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.RecentlyViewed;
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
@DiscriminatorColumn(name = "dtype")
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

    @Column(nullable = false)
    protected Long important;

    @ManyToOne
    protected User user;

    @ManyToOne
    protected Group group;
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<File> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "basicBoard", cascade = {CascadeType.ALL})
    private List<Hashtag> hashtagList = new ArrayList<>();
    @OneToMany(mappedBy = "basicBoard")
    private List<RecentlyViewed> recentlyViewedList;

    public BasicBoard(BoardRequestDto requestDto, User user, Group group) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.subtitle = requestDto.getSubtitle();
        this.important = requestDto.getImportant();
        this.user = user;
        this.group = group;
    }

    public void update(BoardUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.subtitle = requestDto.getSubtitle();
        this.important = requestDto.getImportant();
    }

    public void addFile(File boardFile) {
        fileList.add(boardFile);
    }
}
