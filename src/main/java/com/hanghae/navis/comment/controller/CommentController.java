package com.hanghae.navis.comment.controller;

import com.hanghae.navis.comment.dto.CommentResponseDto;
import com.hanghae.navis.comment.service.CommentService;
import com.hanghae.navis.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private CommentService commentService;

    @GetMapping("/{boardId}")
    @Operation(summary = "댓글 리스트", description = "댓글 리스트")
    public List<CommentResponseDto> commentList(@PathVariable Long boardId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 등록", description = "댓글 등록")
    public CommentResponseDto createComment(@PathVariable Long boardId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글 수정(관리자만 가능)")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    public void deleteComment(@PathVariable Long commentId,
                              @Parameter @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return;
    }

}
