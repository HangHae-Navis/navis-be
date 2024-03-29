package com.hanghae.navis.vote.controller;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.vote.dto.OptionRequestDto;
import com.hanghae.navis.vote.dto.PickRequestDto;
import com.hanghae.navis.vote.dto.VoteRequestDto;
import com.hanghae.navis.vote.service.VoteService;
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

@Tag(name = "vote")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/votes")
public class VoteController {

    private final VoteService voteService;

    @GetMapping("")
    @Operation(summary = "투표 전체목록", description = "투표 전체목록")
    public ResponseEntity<Message> getVoteList(@PathVariable Long groupId,
                                               @RequestParam int page,
                                               @RequestParam int size,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.getVoteList(groupId, userDetails.getUser(), page-1, size);
    }

    @GetMapping("/{voteId}")
    @Operation(summary = "투표 상세 조회", description = "투표 상세 조회, 만료시간은 유닉스 시간으로.")
    public ResponseEntity<Message> getVote(@PathVariable Long groupId, @PathVariable Long voteId,
                                           @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.getVote(groupId, voteId, userDetails.getUser());
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "투표등록", description = "투표 등록, 파일 다중 업로드, updateUrlList 빼고 해주세요")
    @ApiRateLimiter(key = "createVote" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> createVote(@PathVariable Long groupId,
                                              @Valid @ModelAttribute VoteRequestDto requestDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.createVote(groupId, requestDto, userDetails.getUser());
    }

//    @PutMapping("/{voteId}")
//    @Operation(summary = "투표 수정", description = "투표 수정")
//    public ResponseEntity<Message> updateBoard(@PathVariable Long groupId,
//                                               @PathVariable Long voteId,
//                                               @RequestPart VoteRequestDto requestDto,
//                                               @ModelAttribute List<MultipartFile> multipartFiles,
//                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return voteService.updateVote(groupId, voteId, requestDto, multipartFiles, userDetails.getUser());
//    }

    @DeleteMapping("/{voteId}")
    @Operation(summary = "투표 삭제", description = "투표 삭제")
    public ResponseEntity<Message> deleteVote(@PathVariable Long groupId,
                                              @PathVariable Long voteId,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.deleteVote(groupId, voteId, userDetails.getUser());
    }

    @DeleteMapping("/{voteId}/unpick")
    @Operation(summary = "투표 선택 취소", description = "투표 선택 취소")
    public ResponseEntity<Message> unPickVote(@PathVariable Long groupId,
                                              @PathVariable Long voteId,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.unPickVote(groupId, voteId, userDetails.getUser());
    }

    @GetMapping(value = "/{voteId}/force-expired")
    @Operation(summary = "투표 강제종료", description = "투표 강제종료")
    public ResponseEntity<Message> forceExpiredVote(@PathVariable Long groupId,
                                                    @PathVariable Long voteId,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.forceExpired(groupId, voteId, userDetails.getUser());
    }

    @PostMapping(value = "/{voteId}/pick")
    @Operation(summary = "투표 선택", description = "투표 선택")
    @ApiRateLimiter(key = "pickVote" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> pickVote(@PathVariable Long groupId,
                                            @PathVariable Long voteId,
                                            @RequestBody PickRequestDto pickRequestDto,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.pickVote(groupId, voteId, pickRequestDto.getVoteOption(), userDetails.getUser());
    }
}
