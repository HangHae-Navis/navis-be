package com.hanghae.navis.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Map<String, SseEmitter> findAll();
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);
    Map<String, Object> findAllEventCacheStartWithByUserId(String userId);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String memberId);
    void deleteAllEventCacheStartWithId(String memberId);
}