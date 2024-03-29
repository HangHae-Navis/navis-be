package com.hanghae.navis.board.controller;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.service.BoardService;
import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "board")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("")
    @Operation(summary = "게시글 목록", description = "게시글 목록")
    public ResponseEntity<Message> boardList(@PathVariable Long groupId,
                                             @RequestParam int page,
                                             @RequestParam int size,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.boardList(groupId, page-1, size, userDetails.getUser());
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회")
    public ResponseEntity<Message> getBoard(@PathVariable Long groupId, @PathVariable Long boardId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getBoard(groupId, boardId, userDetails.getUser());
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 등록", description = "게시글 등록, 파일 다중 업로드")
    @ApiRateLimiter(key = "createBoard" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> createBoard(@PathVariable Long groupId,
                                               @Valid @ModelAttribute BoardRequestDto requestDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(groupId, requestDto, userDetails.getUser());
    }

    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @ApiRateLimiter(key = "updateBoard" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> updateBoard(@PathVariable Long groupId,
                                               @PathVariable Long boardId,
                                               @Valid @ModelAttribute BoardRequestDto requestDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(groupId, boardId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long groupId,
                                               @PathVariable Long boardId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(groupId, boardId, userDetails.getUser());
    }
}
