package com.hanghae.navis.board.entity;

import com.hanghae.navis.board.dto.BoardFileRequestDto;
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

    private String fileTitle;

    private String fileUrl;

    @ManyToOne
    private Board board;

    public BoardFile(String fileTitle, String fileUrl, Board board) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
        this.board = board;
    }
}
