package com.hanghae.navis.user.service;

import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.jwt.JwtUtil;
import com.hanghae.navis.common.security.UserDetailsImpl;
import com.hanghae.navis.common.util.RedisUtil;
import com.hanghae.navis.user.dto.*;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private final S3Uploader s3Uploader;
    @Transactional
    public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
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

    public ResponseEntity<Message> userInfo(User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Long id = user.getId();
        String username = user.getUsername();
        String nickname = user.getNickname();
        String profileImage = user.getProfileImage();

        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto (id, username, nickname, profileImage);
//        return new Message().toResponseEntity(USER_INFO_SUCCESS, userInfoResponseDto);
        return Message.toResponseEntity(USER_INFO_SUCCESS, userInfoResponseDto);
    }


    @Transactional
    public ResponseEntity<Message> profileUpdateUser(ProfileUpdateRequestDto requestDto, User user) throws IOException {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(requestDto.getProfileImage() != null){
            String fileUrl = s3Uploader.upload(requestDto.getProfileImage());
            user.profileImageUpdate(fileUrl);
        }
        if(!requestDto.getNickname().equals("")){
            user.NicknameUpdate(requestDto.getNickname());
        }

        return userInfo(user);
    }
}
