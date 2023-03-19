package com.hanghae.navis.homework.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.homework.dto.HomeworkFileRequestDto;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.homework.dto.HomeworkUpdateRequestDto;
import com.hanghae.navis.homework.service.HomeworkService;
import com.hanghae.navis.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "homework")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/homeworks")
public class HomeworkController {
    private final HomeworkService homeworkService;

    @Operation(summary = "과제 게시글 리스트", description = "과제 게시글 리스트")
    @GetMapping("")
    public ResponseEntity<Message> homeworkList(@PathVariable Long groupId,
                                                @RequestParam int page,
                                                @RequestParam int size,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.homeworkList(groupId, page-1, size, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 상세 조회", description = "과제 게시글 상세 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<Message> getHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.getHomework(groupId, boardId, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 생성", description = "과제 게시글 생성, 일반 유저는 불가능 / 만료일은 유닉스 시간으로 받아옴")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createHomework(@PathVariable Long groupId,
                                                  @ModelAttribute HomeworkRequestDto requestDto,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.createHomework(groupId, requestDto, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 수정", description = "과제 게시글 수정, 일반 유저는 불가능 / 만료일은 유닉스 시간으로 받아옴")
    @PutMapping("/{boardId}")
    public ResponseEntity<Message> updateHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @RequestPart HomeworkUpdateRequestDto requestDto,
                                                  @ModelAttribute List<MultipartFile> multipartFiles,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.updateHomework(groupId, boardId, requestDto, multipartFiles, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 삭제", description = "과제 게시글 삭제, 일반 유저는 불가능")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Message> deleteHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.deleteHomework(groupId, boardId, userDetails.getUser());
    }

    @Operation(summary = "과제 제출", description = "과제 제출")
    @PostMapping(value = "/{boardId}/homeworkSubmit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> submitHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @ModelAttribute HomeworkFileRequestDto requestDto,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.submitHomework(groupId, boardId, requestDto, userDetails.getUser());
    }

    @Operation(summary = "과제 제출 취소", description = "과제 제출 취소(삭제 후 재업로드)")
    @DeleteMapping("/{boardId}/homeworkSubmit/{homeworkSubjectId}")
    public ResponseEntity<Message> submitCancel(@PathVariable Long groupId, @PathVariable Long boardId, @PathVariable Long homeworkSubjectId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.submitCancel(groupId, boardId, homeworkSubjectId,userDetails.getUser());
    }
}
