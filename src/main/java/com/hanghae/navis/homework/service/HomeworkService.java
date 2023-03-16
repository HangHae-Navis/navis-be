package com.hanghae.navis.homework.service;

import com.hanghae.navis.board.dto.*;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.SuccessMessage;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.homework.dto.HomeworkListReponseDto;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.homework.dto.HomeworkResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.repository.HomeworkRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> homeworkList(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<HomeworkListReponseDto> responseList = new ArrayList<>();

        List<Homework> homeworkList = homeworkRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId);

        for(Homework homework : homeworkList) {
            responseList.add(new HomeworkListReponseDto(homework));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getHomework(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

//        Homework homework = homeworkRepository.findById()
        return null;
    }

    @Transactional
    public ResponseEntity<Message> creatHomework(Long groupId, HomeworkRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = new Homework(requestDto, user, group, unixTimeToLocalDateTime(requestDto.getExpirationDate()), false);

        homeworkRepository.save(homework);

        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        try {
            for (MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                String fileUrl = s3Uploader.upload(file);
                File homeworkFile = new File(fileTitle, fileUrl, homework);
                fileRepository.save(homeworkFile);
                fileResponseDto.add(new FileResponseDto(homeworkFile.getFileTitle(), homeworkFile.getFileUrl()));
            }
            HomeworkResponseDto responseDto = new HomeworkResponseDto(homework, fileResponseDto, false, unixTimeToLocalDateTime(requestDto.getExpirationDate()));

            return Message.toResponseEntity(SuccessMessage.BOARD_POST_SUCCESS, responseDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateHomework(Long groupId, Long boardId, BoardUpdateRequestDto updateRequestDto, HomeworkRequestDto homeworkUpdateDto, List<MultipartFile> multipartFiles, User user) {


        return Message.toResponseEntity(BOARD_PUT_SUCCESS);
    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneOffset.UTC);
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }
}