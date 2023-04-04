//package com.hanghae.navis.common.controller;
//
//import com.hanghae.navis.common.service.PubSubService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.Set;
//
//@RequestMapping("/notification")
//@RestController
//@RequiredArgsConstructor
//public class PubSubController {
//
//    private final PubSubService pubSubService;
//    // 토픽 목록
//    @GetMapping("/topics")
//    public Set<String> getTopicAll() {
//        return pubSubService.getTopicAll();
//    }
//    // 토픽 생성
//    @PostMapping("/topics/{name}")
//    public void createTopic(@RequestBody NotificationRequestDto requestDto) {
//        pubSubService.createTopic(requestDto);
//    }
//
//    // 토픽 제거
//    @DeleteMapping("/topics/{name}")
//    public void deleteTopic(@PathVariable String name) {
//        pubSubService.deleteTopic(name);
//    }
//
//    @GetMapping("/test")
//    public ModelAndView chatPage() {
//        return new ModelAndView("notification");
//    }
//}