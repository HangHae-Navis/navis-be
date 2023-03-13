package com.hanghae.navis.user.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.common.util.RedisUtil;
import com.hanghae.navis.user.dto.LoginRequestDto;
import com.hanghae.navis.user.dto.LoginResponseDto;
import com.hanghae.navis.user.dto.SignupRequestDto;
import com.hanghae.navis.user.dto.UserInfoResponseDto;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    @Transactional
    public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            throw new CustomException(DUPLICATE_USER);
        }

        found = userRepository.findByNickname(nickname);
        if (found.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;

        //닉네임이 공백포함인지 확인
        if(nickname.replaceAll(" ","").equals("")) {
            throw new CustomException(NICKNAME_WITH_SPACES);
        }

        User user = new User(username, nickname, password, role);
        userRepository.save(user);

        return Message.toResponseEntity(SIGN_UP_SUCCESS);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        Optional<User> user = userRepository.findByUsername(username);
        if (!(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword()))) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));

        String token = jwtUtil.createToken(user.get().getUsername(), user.get().getRole());
        LoginResponseDto loginResponseDto = new LoginResponseDto(userRepository.findByNickname(user.get().getNickname()).get().getNickname(), token);
        return Message.toResponseEntity(LOGIN_SUCCESS, loginResponseDto);
    }

    public ResponseEntity<Message> userInfo(UserDetailsImpl user) {

        Long id = user.getUser().getId();
        String username = user.getUser().getUsername();
        String nickname = user.getUser().getNickname();

        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto (id, username, nickname);
//        return new Message().toResponseEntity(USER_INFO_SUCCESS, userInfoResponseDto);
        return Message.toResponseEntity(USER_INFO_SUCCESS, userInfoResponseDto);
    }
}
