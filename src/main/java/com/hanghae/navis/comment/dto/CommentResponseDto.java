package com.hanghae.navis.comment.dto;

import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createAt;
    private GroupMemberRoleEnum role;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .createAt(comment.getCreatedAt())
                .build();
    }

    public static Page<CommentResponseDto> toDtoPage(Page<Comment> commentPage) {
        return commentPage.map(CommentResponseDto::of);
    }
}
