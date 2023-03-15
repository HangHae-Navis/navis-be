package com.hanghae.navis.vote.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "vote")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{groupId}/votes")
public class VoteController {
}
