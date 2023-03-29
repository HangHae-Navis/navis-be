package com.hanghae.navis.board.service;

import com.hanghae.navis.board.dto.*;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.*;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.repository.HashtagRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final GroupMemberRepository groupMemberRepository;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;



    @Transactional(readOnly = true)
    public ResponseEntity<Message> boardList(Long groupId, int page, int size, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardList = boardRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId, pageable);

        Page<BoardListResponseDto> boardListResponseDto = BoardListResponseDto.toDtoPage(boardList);

        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, boardListResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getBoard(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        List<String> hashtagResponseDto = new ArrayList<>();
        board.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        board.getHashtagList().forEach(value -> hashtagResponseDto.add(value.getHashtagName()));
        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role);
        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, boardResponseDto);
    }

    @Transactional
    public ResponseEntity<Message>  createBoard(Long groupId, BoardRequestDto requestDto, User user) {
        try {
            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            GroupMemberRoleEnum role = groupMember.getGroupRole();

            Board board = new Board(requestDto, user, group);
            boardRepository.save(board);

            List<String> hashtagResponseDto = new ArrayList<>();

            for(String tag : requestDto.getHashtagList().split(" ")) {
                Hashtag hashtag = new Hashtag(tag, board);
                hashtagRepository.save(hashtag);
                hashtagResponseDto.add(tag);
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();

//            if(requestDto.getMultipartFiles() != null) {
                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File boardFile = new File(fileTitle, fileUrl, board);
                    fileRepository.save(boardFile);
                    fileResponseDto.add(FileResponseDto.of(boardFile));
                }
//            }
            BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role);
            return Message.toResponseEntity(BOARD_POST_SUCCESS, boardResponseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateBoard(Long groupId, Long boardId, BoardRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if(!user.getId().equals(board.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, null, null, role);

        board.update(requestDto);

        List<Hashtag> remainTag = hashtagRepository.findAllByBasicBoardId(boardId);

        for (Hashtag hashtag : remainTag) {
            board.getFileList().remove(hashtag);
            hashtagRepository.delete(hashtag);
        }

        List<String> hashtagResponseDto = new ArrayList<>();

        for(String tag : requestDto.getHashtagList().split(" ")) {
            Hashtag hashtag = new Hashtag(tag, board);
            hashtagRepository.save(hashtag);
            hashtagResponseDto.add(tag);
        }

        List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();
        List<File> files = fileRepository.findFileUrlByBasicBoardId(boardId);

        try {
            for(File boardFile : files) {
                if(!multipartFiles.contains(boardFile.getFileUrl())) {
                    board.getFileList().remove(boardFile);
                    String source = URLDecoder.decode(boardFile.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                    fileRepository.delete(boardFile);
                }
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            if (requestDto.getMultipartFiles() != null) {

                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File boardFile = new File(fileTitle, fileUrl, board);
                    fileRepository.save(boardFile);
                    fileResponseDto.add(new FileResponseDto(boardFile.getFileTitle(), boardFile.getFileUrl()));
                }
            }
            boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Message.toResponseEntity(BOARD_PUT_SUCCESS, boardResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteBoard(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT) && !user.getId().equals(board.getUser().getId())) {
            throw new CustomException(USER_FORBIDDEN);
        }

        if(board.getFileList().size() > 0) {
            try {
                for (File file : board.getFileList()) {
                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        boardRepository.deleteById(boardId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> deleteHashtag(Long groupId, Long hashtagId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(
                () -> new CustomException(HASHTAG_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        hashtagRepository.deleteById(hashtagId);
        return Message.toResponseEntity(HASHTAG_DELETE_SUCCESS);
    }

    public UserGroup authCheck(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (groupMember.getGroupRole().getAuthority().equals(UserRoleEnum.Authority.USER)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        return new UserGroup(user, group);
    }
}
