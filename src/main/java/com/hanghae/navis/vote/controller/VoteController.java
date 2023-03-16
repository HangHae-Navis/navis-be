package com.hanghae.navis.vote.controller;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
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

import java.util.List;

@Tag(name = "vote")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/votes")
public class VoteController {

    private final VoteService voteService;
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "투표등록", description = "투표 등록, 파일 다중 업로드")
    public ResponseEntity<Message> createBoard(@PathVariable Long groupId,
                                               @RequestPart VoteRequestDto requestDto,
                                               @ModelAttribute List<MultipartFile> multipartFiles,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return voteService.createVote(groupId, requestDto, multipartFiles, userDetails.getUser());
    }
}
