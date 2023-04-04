//package com.hanghae.navis.common.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RedisSubscriber implements MessageListener {
//    private final ObjectMapper objectMapper;
//    private final RedisTemplate redisTemplate;
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            String sendMessage = (String) redisTemplate.getStringSerializer()
//                    .deserialize(message.getBody());
//
//            String data = objectMapper.readValue(sendMessage, String.class);
//            System.out.println(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
