//package com.hanghae.navis.common.service;
//
//import com.hanghae.navis.common.dto.NotificationResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class PubSubService {
//    private final RedisMessageListenerContainer redisMessageListener;
//    private final RedisPublisher redisPublisher;
//    private final RedisSubscriber redisSubscriber;
//    private Map<String, ChannelTopic> channels = new HashMap<>();
//
//    // 토픽 목록
//    public Set<String> getTopicAll() {
//        return channels.keySet();
//    }
//
//    // 토픽 생성
//    public void createTopic(NotificationRequestDto requestDto) {
//        String topicName = requestDto.getTopicName();
//
//        if (channels.get(topicName) == null) {
//            ChannelTopic channel = new ChannelTopic(topicName);
//            redisMessageListener.addMessageListener(redisSubscriber, channel);
//            channels.put(topicName, channel);
//        }
//    }
//
//    // 메시지 발행
//    public void pushMessage(String name, NotificationResponseDto notificationResponseDto) {
//        ChannelTopic channel = channels.get(name);
//        redisPublisher.publish(channel, notificationResponseDto);
//    }
//
//    // 토픽 제거
//    public void deleteTopic(String name) {
//        ChannelTopic channel = channels.get(name);
//        redisMessageListener.removeMessageListener(redisSubscriber, channel);
//        channels.remove(name);
//    }
//
//}
