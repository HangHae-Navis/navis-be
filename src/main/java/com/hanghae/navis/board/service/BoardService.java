package com.hanghae.navis.board.service;

import com.hanghae.navis.board.dto.BoardListResponseDto;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.BoardFile;
import com.hanghae.navis.board.repository.BoardFileRepository;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.QBasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final S3Uploader s3Uploader;



    @Transactional(readOnly = true)
    public ResponseEntity<Message> boardList(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<BoardListResponseDto> responseList = new ArrayList<>();

        List<Board> boardList = boardRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId);

        for (Board board : boardList) {
            responseList.add(new BoardListResponseDto(board));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
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

        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, new BoardResponseDto(board));
    }

    @Transactional
    public ResponseEntity<Message> createBoard(Long groupId, BoardRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            Board board = new Board(requestDto, user, group);
            boardRepository.save(board);

            for (MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                String fileUrl = s3Uploader.upload(file);
                BoardFile boardFile = new BoardFile(fileTitle, fileUrl, board);
                fileRepository.save(boardFile);
                board.addFile(boardFile);
            }

            return Message.toResponseEntity(BOARD_POST_SUCCESS, new BoardResponseDto(board));
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

        if (!user.getUsername().equals(board.getUser().getUsername())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        board.update(requestDto);

        List<String> remainUrl = requestDto.getUpdateUrlList();

        List<BoardFile> files = fileRepository.findFileUrlByBoardId(boardId);

        try {
            for(BoardFile boardFile : files) {
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
                if(!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()) {
                    for (MultipartFile file : multipartFiles) {
                        String fileTitle = file.getOriginalFilename();
                        String fileUrl = s3Uploader.upload(file);
                        BoardFile boardFile = new BoardFile(fileTitle, fileUrl, board);
                        fileRepository.save(boardFile);
                        board.addFile(boardFile);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Message.toResponseEntity(BOARD_PUT_SUCCESS, new BoardResponseDto(board));
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
                for (BoardFile boardFile : board.getFileList()) {
                    String source = URLDecoder.decode(boardFile.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        boardRepository.deleteById(boardId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }
}
