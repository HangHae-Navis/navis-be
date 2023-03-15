package com.hanghae.navis.group.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "group")
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("")
    @Operation(summary = "그룹 생성", description = "그룹 생성")
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGroup(requestDto, userDetails.getUser());
    }

    @PostMapping("/apply")
    @Operation(summary = "그룹 참여", description = "그룹 참여")
    public ResponseEntity<Message> applyGroup(@RequestBody ApplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.applyGroup(requestDto, userDetails.getUser());
    }

//    @GetMapping("")
//    @ResponseBody
//    public ResponseEntity<Message<Page<GroupResponseDto>>> getGroups()
}
