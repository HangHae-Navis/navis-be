package com.hanghae.navis.board.controller;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.service.BoardService;
import com.hanghae.navis.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private BoardService boardService;

    @GetMapping("/")
    @Operation(summary = "게시글 목록", description = "게시글 목록")
    public List<BoardResponseDto> boardlist(@Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 등록", description = "게시글 등록")
    public BoardResponseDto createBoard(@ModelAttribute BoardRequestDto requestDto,
                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정" ,description = "게시글 수정")
    public BoardResponseDto updateBoard(@PathVariable Long boardId,
                                        @ModelAttribute BoardRequestDto requestDto,
                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    public void deleteBoard(@PathVariable Long boardId,
                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return;
    }
}
