package com.hanghae.navis.email.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@Tag(name = "email")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;
    @GetMapping("/confirm")
    @Operation(summary = "코드 확인", description ="코드확인")
    public ResponseEntity<Message> emailConfirm(@RequestParam String key){
        return emailService.emailConfirm(key);
    }

    @PostMapping("/confirm")
    @Operation(summary = "인증 코드 전송", description ="인증 코드 전송")
    public ResponseEntity<Message> sendMail(@RequestParam @Email String email) throws Exception {
        return emailService.sendMail(email);
    }
}
