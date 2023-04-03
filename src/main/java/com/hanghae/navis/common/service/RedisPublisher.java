package com.hanghae.navis.common.service;

import com.hanghae.navis.common.dto.NotificationResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, NotificationResponseDto responseDto) {
        redisTemplate.convertAndSend(topic.getTopic(), responseDto);
    }
}