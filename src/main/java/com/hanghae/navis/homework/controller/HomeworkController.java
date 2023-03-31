package com.hanghae.navis.homework.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.homework.dto.HomeworkFileRequestDto;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.homework.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @Operation(summary = "과제 게시글 상세 조회", description = "ADMIN, SUPPORT : 상세 게시글과 그룹의 유저 리스트 return / USER : 상세 게시글만 return")
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
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> updateHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @ModelAttribute HomeworkRequestDto requestDto,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.updateHomework(groupId, boardId, requestDto, userDetails.getUser());
    }

    @Operation(summary = "과제 게시글 삭제", description = "과제 게시글 삭제, 일반 유저는 불가능")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Message> deleteHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.deleteHomework(groupId, boardId, userDetails.getUser());
    }

    @Operation(summary = "과제 제출", description = "과제 제출, 이미 제출한 사람은 제출 취소를 하고 다시 제출해야함")
    @PostMapping(value = "/{boardId}/homeworkSubmit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> submitHomework(@PathVariable Long groupId, @PathVariable Long boardId,
                                                  @ModelAttribute HomeworkFileRequestDto requestDto,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.submitHomework(groupId, boardId, requestDto, userDetails.getUser());
    }

    @Operation(summary = "과제 제출 취소", description = "과제 제출 취소(삭제 후 재업로드)")
    @DeleteMapping("/{boardId}/cancel")
    public ResponseEntity<Message> submitCancel(@PathVariable Long groupId, @PathVariable Long boardId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return homeworkService.submitCancel(groupId, boardId, userDetails.getUser());
    }

    @Operation(summary = "제출된 과제 파일 다운로드", description = "제출된 과제 파일 다운로드")
    @PostMapping("/{boardId}/download/{fileName}")
    public ResponseEntity<Message> downloadFile(@PathVariable Long groupId, @PathVariable Long boardId, @PathVariable String fileName) throws IOException {
        return homeworkService.downloadFile(groupId, boardId, fileName);
    }
}
