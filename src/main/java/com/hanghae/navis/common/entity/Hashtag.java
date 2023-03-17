package com.hanghae.navis.common.entity;

import com.hanghae.navis.board.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "hashtag")
@Getter
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashtagName;

    @ManyToOne
    private BasicBoard basicBoard;

    public Hashtag(String hashtagName, BasicBoard basicBoard) {
        this.hashtagName = hashtagName;
        this.basicBoard = basicBoard;
    }
}
