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
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
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

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();
        board.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        board.getHashtagList().forEach(value -> hashtagResponseDto.add(HashtagResponseDto.of(value)));
        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto);
        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, boardResponseDto);
    }

    @Transactional
    public ResponseEntity<Message>  createBoard(Long groupId, BoardRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            Board board = new Board(requestDto, user, group);
            boardRepository.save(board);

            List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();

            for(HashtagRequestDto hashtagRequestDto : requestDto.getHashtagList()) {
                String tag = hashtagRequestDto.getHashtag();
                Hashtag hashtag = new Hashtag(tag, board);
                hashtagRepository.save(hashtag);
                hashtagResponseDto.add(new HashtagResponseDto(tag));
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            if(multipartFiles != null) {
                for (MultipartFile file : multipartFiles) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File boardFile = new File(fileTitle, fileUrl, board);
                    fileRepository.save(boardFile);
                    fileResponseDto.add(FileResponseDto.of(boardFile));
                }
            }
            BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto);
            return Message.toResponseEntity(BOARD_POST_SUCCESS, boardResponseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateBoard(Long groupId, Long boardId, BoardUpdateRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, null, null);

        if (!user.getUsername().equals(board.getUser().getUsername())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        board.update(requestDto);

        List<Hashtag> remainTag = hashtagRepository.findAllByBasicBoardId(boardId);

        for (Hashtag hashtag : remainTag) {
            board.getFileList().remove(hashtag);
            hashtagRepository.delete(hashtag);
        }

        List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();

        for(HashtagRequestDto hashtagRequestDto : requestDto.getHashtagList()) {
            String tag = hashtagRequestDto.getHashtag();
            Hashtag hashtag = new Hashtag(tag, board);
            hashtagRepository.save(hashtag);
            hashtagResponseDto.add(new HashtagResponseDto(tag));
        }

        List<String> remainUrl = requestDto.getUpdateUrlList();

        List<File> files = fileRepository.findFileUrlByBasicBoardId(boardId);

        try {
            for(File boardFile : files) {
                if(!remainUrl.contains(boardFile.getFileUrl())) {
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
            if(multipartFiles != null) {
                List<FileResponseDto> fileResponseDto = new ArrayList<>();
                if(!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()) {
                    for (MultipartFile file : multipartFiles) {
                        String fileTitle = file.getOriginalFilename();
                        String fileUrl = s3Uploader.upload(file);
                        File boardFile = new File(fileTitle, fileUrl, board);
                        fileRepository.save(boardFile);
                        fileResponseDto.add(new FileResponseDto(boardFile.getFileTitle(), boardFile.getFileUrl()));
                    }
                    boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto);
                }

            }
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

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (!user.getId().equals(board.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
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
}
