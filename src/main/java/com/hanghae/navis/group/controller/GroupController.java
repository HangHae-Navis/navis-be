package com.hanghae.navis.group.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "group")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "그룹 개설", description ="그룹명 필수, groupInfo는 생략 가능")
    public ResponseEntity<Message> createGroup(@ModelAttribute GroupRequestDto requestDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGroup(requestDto, userDetails.getUser());
    }

    @PostMapping("/apply")
    @Operation(summary = "그룹 가입", description ="그룹 코드를 입력해 가입")
    public ResponseEntity<Message> applyGroup(@RequestBody ApplyRequestDto requestDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.applyGroup(requestDto, userDetails.getUser());
    }

    @GetMapping("")
    @ResponseBody
    @Operation(summary = "그룹리스트 조회", description ="category- All:전체 조회, myOwn:자신이 Admin인 그룹 조회, joined:자신이 가입한 그룹 조회")
    public ResponseEntity<Message> getGroups(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false, defaultValue = "all") String category,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getGroups(page-1, size, category, userDetails.getUser());
    }

    @GetMapping("/{groupId}")
    @ResponseBody
    @Operation(summary = "그룹 메인화면 조회", description = "그룹 메인화면 조회, 회원만 가능, category: all(기본), board, homework, vote")
    public ResponseEntity<Message> getGroupMainPage(@PathVariable Long groupId,
                                                    @RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam(required = false, defaultValue = "all") String category,
                                                    @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getGroupMainPage(groupId, page-1, size, category, sortBy, userDetails.getUser());
    }


    @GetMapping("/{groupId}/admin")
    @ResponseBody
    @Operation(summary = "그룹 관리자 페이지", description ="그룹 정보 상세조회, Admin만 가능")
    public ResponseEntity<Message> getGroupDetails(@PathVariable Long groupId,
                                                  @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getGroupDetails(groupId, userDetails.getUser());
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "그룹 탈퇴", description ="멤버 id를 입력할 경우 ADMIN의 강퇴 기능, 입력하지 않으면 일반 유저의 탈퇴 기능, 자신이 ADMIN일 경우 탈퇴 불가")
    public ResponseEntity<Message> deleteGroupMember(@PathVariable Long groupId,
                                                     @RequestParam(required = false) Long memberId,
                                                     @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.deleteGroupMember(groupId, memberId, userDetails.getUser());
    }

    @DeleteMapping("/{groupId}/admin")
    @Operation(summary = "그룹 삭제", description ="ADMIN만 가능")
    public ResponseEntity<Message> deleteGroup(@PathVariable Long groupId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.deleteGroup(groupId, userDetails.getUser());
    }

}
