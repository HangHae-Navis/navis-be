package com.hanghae.navis.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.email.service.EmailService;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.user.dto.FindPasswordRequestDto;
import com.hanghae.navis.user.dto.LoginRequestDto;
import com.hanghae.navis.user.dto.ProfileUpdateRequestDto;
import com.hanghae.navis.user.dto.SignupRequestDto;
import com.hanghae.navis.user.service.KakaoService;
import com.hanghae.navis.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static com.hanghae.navis.common.entity.ExceptionMessage.USER_FORBIDDEN;


@Tag(name = "user")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/login-page")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    @Operation(summary = "로그인", description ="로그인")

    public ResponseEntity<Message> login(@RequestBody LoginRequestDto loginRequestDto, @Parameter(hidden = true) HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 업데이트", description ="프로필 업데이트")
    public ResponseEntity<Message> profileUpdateUser(@ModelAttribute ProfileUpdateRequestDto requestDto,
                                                     @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.profileUpdateUser(requestDto, userDetails.getUser());
    }

    @GetMapping("/kakao/callback")
    @Operation(hidden = true)
    public ResponseEntity<Message> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }

    @RequestMapping("/forbidden")
    @Operation(hidden = true)
    public ResponseEntity<Message> getForbidden() {
        throw new CustomException(USER_FORBIDDEN);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Message> userInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userInfo(userDetails.getUser());
    }
    @ResponseBody
    @GetMapping("/search")
    public ResponseEntity<Message> searchUser(@RequestParam String searchName, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.searchUser(searchName, userDetails.getUser());
    }
    @GetMapping("/forgetPassword")
    @Operation(hidden = true)
    public ResponseEntity<Message> forgetPassword(@RequestBody FindPasswordRequestDto findPasswordRequestDto) throws JsonProcessingException {
        return userService.findPassword(findPasswordRequestDto);
    }
}
