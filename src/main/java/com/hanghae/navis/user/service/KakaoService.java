package com.hanghae.navis.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.homework.service.HomeworkService;
import com.hanghae.navis.messenger.service.MessengerService;
import com.hanghae.navis.user.dto.KakaoUserInfoDto;
import com.hanghae.navis.user.dto.LoginResponseDto;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static com.hanghae.navis.common.entity.ExceptionMessage.MEMBER_NOT_FOUND;
import static com.hanghae.navis.common.entity.ExceptionMessage.USER_DELETE_FAIL;
import static com.hanghae.navis.common.entity.SuccessMessage.LOGIN_SUCCESS;
import static com.hanghae.navis.common.entity.SuccessMessage.USER_DELETE_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessengerService messengerService;
    private final HomeworkService homeworkService;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<Message> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);
        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);
        LoginResponseDto loginResponseDto = new LoginResponseDto(kakaoUser, createToken);
        return Message.toResponseEntity(LOGIN_SUCCESS, loginResponseDto);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "824bc0f4442a06c2905b527703106262");
        body.add("redirect_uri", "http://navis.kro.kr");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        String email = "";
        if (jsonNode.get("kakao_account").get("email") != null) {
            email = jsonNode.get("kakao_account").get("email").asText();
        }

        if (email.equals("")) {
            email = UUID.randomUUID().toString();
        }

        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3. 필요시에 회원가입
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByUsername(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.updateKakaoId(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(email, kakaoUserInfo.getNickname(), kakaoId, encodedPassword, UserRoleEnum.USER);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    @Transactional
    public ResponseEntity<Message> unlink(String token, User user) throws IOException {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        conn.getResponseCode();

        if (messengerService.deleteLeaveMessenger(user) && homeworkService.userLeaveDeleteSubject(user)) {
            userRepository.delete(user);
            return Message.toResponseEntity(USER_DELETE_SUCCESS);
        }else {
            return Message.toExceptionResponseEntity(USER_DELETE_FAIL);
        }
    }
}
