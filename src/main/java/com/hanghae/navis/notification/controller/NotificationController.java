package com.hanghae.navis.notification.controller;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    
    @Operation(summary = "알림 구독", description = "알림 구독하기")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getUser(), lastEventId);
    }

    @Operation(summary = "알림 가져오기", description = "알림 가져오기")
    @GetMapping(value = "api/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Message> getNotification(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.getNotification(userDetails.getUser());
    }

    @Operation(summary = "알림 삭제하기", description = "알림 삭제하기")
    @DeleteMapping(value = "api/subscribe/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Message> deleteNotification(@PathVariable(value = "notificationId") String notificationId, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.deleteNotification(notificationId, userDetails.getUser());
    }

    @Operation(summary = "알림 전체 삭제하기", description = "알림 전체 삭제하기")
    @DeleteMapping(value = "api/subscribe/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Message> deleteAllNotification (@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.deleteAllNotification(userDetails.getUser());
    }
}
