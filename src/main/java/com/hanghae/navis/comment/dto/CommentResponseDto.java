package com.hanghae.navis.comment.dto;

import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.user.entity.User;
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
    private String profileImage;
    private String content;
    private String nickname;
    private LocalDateTime createAt;
    private boolean isOwned;

    public static CommentResponseDto of(Comment comment, User user) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .profileImage(comment.getUser().getProfileImage())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .createAt(comment.getCreatedAt())
                .isOwned(comment.getUser().getId().equals(user.getId()))
                .build();
    }

    public static Page<CommentResponseDto> toDtoPage(Page<Comment> commentPage, User user) {
        return commentPage.map(value -> CommentResponseDto.of(value, user));
    }
}
