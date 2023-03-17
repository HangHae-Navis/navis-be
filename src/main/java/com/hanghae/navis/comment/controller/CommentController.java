package com.hanghae.navis.comment.controller;

import com.amazonaws.Response;
import com.hanghae.navis.comment.dto.CommentRequestDto;
import com.hanghae.navis.comment.dto.CommentResponseDto;
import com.hanghae.navis.comment.service.CommentService;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/{boardId}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("")
    @Operation(summary = "댓글 리스트", description = "댓글 리스트")
    public ResponseEntity<Message> commentList(@PathVariable Long groupId, @PathVariable Long boardId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commentList(groupId, boardId, userDetails.getUser());
    }

    @PostMapping("")
    @Operation(summary = "댓글 등록", description = "댓글 등록")
    public ResponseEntity<Message> createComment(@PathVariable Long groupId, @PathVariable Long boardId,
                                                 @RequestBody CommentRequestDto requestDto,
                                                 @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(groupId, boardId, requestDto, userDetails.getUser());
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글 수정(관리자만 가능)")
    public ResponseEntity<Message> updateComment(@PathVariable Long groupId, @PathVariable Long boardId, @PathVariable Long commentId,
                                                 @RequestBody CommentRequestDto requestDto,
                                                 @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(groupId, boardId, commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    public ResponseEntity<Message> deleteComment(@PathVariable Long groupId, @PathVariable Long boardId, @PathVariable Long commentId,
                                                 @Parameter @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(groupId, boardId, commentId, userDetails.getUser());
    }

}
