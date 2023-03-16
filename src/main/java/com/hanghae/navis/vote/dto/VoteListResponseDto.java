package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.vote.entity.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class VoteListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    public VoteListResponseDto(Vote vote) {
        this.id = vote.getId();
        this.subtitle = vote.getSubtitle();
        this.title = vote.getTitle();
        this.content = vote.getContent();
        this.nickName = vote.getUser().getNickname();
        this.groupName = vote.getGroup().getGroupName();
        this.createAt = vote.getCreatedAt();
    }
}
