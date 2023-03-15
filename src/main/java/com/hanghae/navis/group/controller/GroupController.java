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
import org.springframework.data.domain.Page;
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

    @PostMapping("")
    @Operation(summary = "그룹 개설", description ="그룹명 필수, groupInfo는 생략 가능")
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto,
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
}
