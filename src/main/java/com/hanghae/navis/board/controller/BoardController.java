package com.hanghae.navis.board.controller;

import com.amazonaws.services.ec2.model.InstanceMetadataEndpointState;
import com.hanghae.navis.board.dto.BoardFileRequestDto;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.entity.BoardFile;
import com.hanghae.navis.board.service.BoardService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "board")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/")
    @Operation(summary = "게시글 목록", description = "게시글 목록")
    public ResponseEntity<Message> boardList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.boardList();
    }

    @PostMapping("/posts")
    @Operation(summary = "게시글 등록", description = "게시글 등록, 파일 다중 업로드")
    public ResponseEntity<Message> createBoard(@RequestPart BoardRequestDto requestDto,
                                               @RequestPart("multipartFiles") List<MultipartFile> multipartFiles,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, multipartFiles, userDetails.getUser());
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
