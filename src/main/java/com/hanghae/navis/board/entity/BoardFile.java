package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "boardFile")
@Getter
@NoArgsConstructor
public class BoardFile extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne
    private Board board;

    public BoardFile(BoardRequestDto requestDto, Board board) {
        this.fileUrl = String.valueOf(requestDto.getFileUrl());
        this.board = board;
    }
}
