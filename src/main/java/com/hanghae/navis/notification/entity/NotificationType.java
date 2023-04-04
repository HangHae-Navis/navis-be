package com.hanghae.navis.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {

    HOMEWORK_POST("homework", "과제가 새로 올라왔습니다.");

    private final String type;
    private final String content;

    NotificationType(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
