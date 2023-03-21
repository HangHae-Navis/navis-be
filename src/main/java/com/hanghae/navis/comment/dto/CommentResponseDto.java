package com.hanghae.navis.comment.dto;

import com.hanghae.navis.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createAt;
    private boolean owned = false;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .createAt(comment.getCreatedAt())
                .build();
    }

    public CommentResponseDto(Comment comment, boolean owned) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.createAt = comment.getCreatedAt();
        this.owned = owned;
    }

    public static Page<CommentResponseDto> toDtoPage(Page<Comment> commentPage) {
        return commentPage.map(CommentResponseDto::of);
    }
}
