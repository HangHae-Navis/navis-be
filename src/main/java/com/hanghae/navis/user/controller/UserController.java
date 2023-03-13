package com.hanghae.navis.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.email.service.EmailService;
import com.hanghae.navis.user.dto.LoginRequestDto;
import com.hanghae.navis.user.dto.SignupRequestDto;
import com.hanghae.navis.user.service.KakaoService;
import com.hanghae.navis.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.hanghae.navis.common.entity.ExceptionMessage.USER_FORBIDDEN;


@Tag(name = "user")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
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
    @GetMapping("/kakao/callback")
    @Operation(hidden = true)
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String createToken = kakaoService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("http://hanghae1teamwork.s3-website.ap-northeast-2.amazonaws.com/");
        response.addCookie(cookie);

        return "redirect:http://hanghae1teamwork.s3-website.ap-northeast-2.amazonaws.com/";
    }

    @RequestMapping("/forbidden")
    @Operation(hidden = true)
    public ResponseEntity<Message> getForbidden() {
        throw new CustomException(USER_FORBIDDEN);
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Message> userInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userInfo(userDetails);
    }
}
