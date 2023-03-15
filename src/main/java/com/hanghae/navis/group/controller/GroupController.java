package com.hanghae.navis.group.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.service.GroupService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("")
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGroup(requestDto, userDetails.getUser());
    }

    @PostMapping("/apply")
    public ResponseEntity<Message> applyGroup(@RequestBody ApplyRequestDto requestDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.applyGroup(requestDto, userDetails.getUser());
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Message> getGroups(@RequestParam int page,
                                                                     @RequestParam int size,
                                                                     @RequestParam(required = false, defaultValue = "all") String category,
                                                                     @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getGroups(page-1, size, category, userDetails.getUser());
    }
}
