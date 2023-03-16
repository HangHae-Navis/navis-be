package com.hanghae.navis.homework.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
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
    @GetMapping("/")
    public ResponseEntity<Message> homeworkList(@PathVariable Long groupId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.homeworkList(groupId, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 상세 조회", description = "과제 게시글 상세 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<Message> getHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.getHomework(groupId, boardId, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 생성", description = "과제 게시글 생성, 일반 유저는 불가능 / 만료일은 유닉스 시간으로 받아옴")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createHomework(@PathVariable Long groupId,
                                                  @RequestPart HomeworkRequestDto requestDto,
                                                  @ModelAttribute List<MultipartFile> multipartFiles,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.creatHomework(groupId, requestDto, multipartFiles, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 수정", description = "과제 게시글 수정, 일반 유저는 불가능")
    @PutMapping("/{boardId}")
    public ResponseEntity<Message> updateHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @RequestPart BoardUpdateRequestDto updateRequestDto,
                                                  @RequestPart HomeworkRequestDto homeworkUpdateDto,
                                                  @ModelAttribute List<MultipartFile> multipartFiles,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.updateHomework(groupId, boardId, updateRequestDto, homeworkUpdateDto, multipartFiles, userDetails.getUser());
    }


}
