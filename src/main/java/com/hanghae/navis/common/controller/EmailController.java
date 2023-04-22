package com.hanghae.navis.common.controller;

import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "email")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;
    @GetMapping("/confirm")
    @Operation(summary = "코드 확인", description ="코드확인")
    @ApiRateLimiter(key = "emailConfirm" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> emailConfirm(@RequestParam String key){
        return emailService.emailConfirm(key);
    }

    @GetMapping("/sendmail")
    @Operation(summary = "인증 코드 전송", description ="인증 코드 전송")
    @ApiRateLimiter(key = "sendMail" + "#{request.remoteAddr}", limit = 1, seconds = 1)
    public ResponseEntity<Message> sendMail(@RequestParam String email) throws Exception {
        return emailService.sendMail(email);
    }
}
