package com.hanghae.navis.common.entity;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "file")
@Getter
@NoArgsConstructor
public class File extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileTitle;

    private String fileUrl;

    @ManyToOne
    private BasicBoard basicBoard;

    public File(String fileTitle, String fileUrl, BasicBoard basicBoard) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
        this.basicBoard = basicBoard;
    }

    public void updateFile(String fileTitle, String fileUrl) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
    }
}
