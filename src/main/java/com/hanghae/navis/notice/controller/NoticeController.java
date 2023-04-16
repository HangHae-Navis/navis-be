package com.hanghae.navis.notice.controller;

import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.notice.dto.NoticeRequestDto;
import com.hanghae.navis.notice.dto.NoticeUpdateRequestDto;
import com.hanghae.navis.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "notice")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("")
    @Operation(summary = "공지사항 목록", description = "공지사항 목록")
    public ResponseEntity<Message> noticeList(@PathVariable Long groupId,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.noticeList(groupId, userDetails.getUser());
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    public ResponseEntity<Message> getNotice(@PathVariable Long groupId, @PathVariable Long noticeId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.getNotice(groupId, noticeId, userDetails.getUser());
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "공지사항 등록", description = "공지사항 등록, 파일 다중 업로드")
    @ApiRateLimiter(key = "createNotice" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> createNotice(@PathVariable Long groupId,
                                                @Valid @ModelAttribute NoticeRequestDto requestDto,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.createNotice(groupId, requestDto, userDetails.getUser());
    }

    @PutMapping("/{noticeId}")
    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    @ApiRateLimiter(key = "updateNotice" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> updateNotice(@PathVariable Long groupId,
                                                @PathVariable Long noticeId,
                                                @Valid @ModelAttribute NoticeUpdateRequestDto requestDto,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.updateNotice(groupId, noticeId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    public ResponseEntity<Message> deleteNotice(@PathVariable Long groupId,
                                                @PathVariable Long noticeId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.deleteNotice(groupId, noticeId, userDetails.getUser());
    }

    @DeleteMapping("/hashtag/{hashtagId}")
    @Operation(summary = "해시태그 삭제", description = "해시태그 삭제")
    public ResponseEntity<Message> deleteHashtag(@PathVariable Long groupId,
                                                 @PathVariable Long hashtagId,
                                                 @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.deleteHashtag(groupId, hashtagId, userDetails.getUser());
    }
}
