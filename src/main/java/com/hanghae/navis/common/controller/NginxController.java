package com.hanghae.navis.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class NginxController {
    //무중단배포 테스트를 위한 version 확인용. 새로고침하면 return 값이 자동으로 바뀌는지
    @GetMapping("/version")
    public String checkVersion() {
        return "ver1";
    }

}