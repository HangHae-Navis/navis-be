package com.hanghae.navis.survey.controller;

import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.survey.dto.FormRequestDto;
import com.hanghae.navis.survey.dto.SurveyRequestDto;
import com.hanghae.navis.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "survey")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/surveys")
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping("")
    @Operation(summary = "설문 게시글 등록", description = "설문 등록, 텍스트 형식만 작성 가능 / subtitle, content, important, multipartFiles는 지우고 테스트해주세요 / option은 체크박스랑 객관식에만 사용, 서술식은 null")
    @ApiRateLimiter(key = "createSurvey" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> createSurvey(@PathVariable Long groupId,
                                                @Valid @RequestBody SurveyRequestDto requestDto,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.createSurvey(groupId, requestDto, userDetails.getUser());
    }

    @GetMapping("/{surveyId}")
    @Operation(summary = "설문 상세 조회", description = "ADMIN, SUPPORT : 투표 통계 조회 / USER : 본인이 작성한 설문 내용 조회")
    public ResponseEntity<Message> getSurvey(@PathVariable Long groupId, @PathVariable Long surveyId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.getSurvey(groupId, surveyId, userDetails.getUser());
    }

    @DeleteMapping("/{surveyId}")
    @Operation(summary = "등록한 설문 게시글 삭제", description = "등록한 설문 게시글 삭제")
    public ResponseEntity<Message> deleteSurvey(@PathVariable Long groupId, @PathVariable Long surveyId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.deleteSurvey(groupId, surveyId, userDetails.getUser());
    }

    @GetMapping("/{surveyId}/force-expired")
    @Operation(summary = "설문 강제 종료", description = "설문 강제 종료")
    public ResponseEntity<Message> forceExpiredSurvey(@PathVariable Long groupId, @PathVariable Long surveyId,
                                                      @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.forceExpiredSurvey(groupId, surveyId, userDetails.getUser());
    }

    @PostMapping("/{surveyId}/fillForm")
    @Operation(summary = "설문 작성", description = "설문 작성")
    @ApiRateLimiter(key = "fillForm" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> fillForm(@PathVariable Long groupId, @PathVariable Long surveyId,
                                            @RequestBody FormRequestDto requestDto,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.fillForm(groupId, surveyId, requestDto, userDetails.getUser());
    }

    @PutMapping("/{surveyId}/updateForm")
    @Operation(summary = "설문 답변 수정", description = "유저가 작성한 설문 답변을 수정")
    @ApiRateLimiter(key = "updateForm" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> updateForm(@PathVariable Long groupId, @PathVariable Long surveyId,
                                              @RequestBody FormRequestDto requestDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.updateForm(groupId, surveyId, requestDto, userDetails.getUser());
    }

    @GetMapping("/{surveyId}/surveyStatDetails/{userId}")
    @Operation(summary = "설문 답변 상세 보기", description = "응답한 답변 상세보기")
    public ResponseEntity<Message> answerDetails (@PathVariable Long groupId, @PathVariable Long surveyId, @PathVariable Long userId,
                                                     @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return surveyService.answerDetails(groupId, surveyId, userId, userDetails.getUser());
    }
}
