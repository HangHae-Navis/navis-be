package com.hanghae.navis.board.service;

import com.hanghae.navis.board.dto.BoardListResponseDto;
import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.BoardFile;
import com.hanghae.navis.board.repository.BoardFileRepository;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> boardList() {
        List<BoardListResponseDto> responseList = new ArrayList<>();
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();

        for (Board board : boardList) {
            responseList.add(new BoardListResponseDto(board));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
    }

    @Transactional
    public ResponseEntity<Message> createBoard(BoardRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );
            Board board = new Board(requestDto, user);
            boardRepository.save(board);

            for(MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                System.out.println(fileTitle);
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

    public ResponseEntity<Message> updateBoard(Long boardId, BoardRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Board board = boardRepository.findById(boardId).orElseThrow(
                    () -> new CustomException(BOARD_NOT_FOUND)
            );

            for(MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                System.out.println(fileTitle);
                String fileUrl = s3Uploader.upload(file);
                BoardFile boardFile = new BoardFile(fileTitle, fileUrl, board);
                fileRepository.save(boardFile);
                board.addFile(boardFile);
            }
            boardRepository.save(board);

            return Message.toResponseEntity(BOARD_POST_SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Message> deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(!user.getId().equals(board.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        boardRepository.deleteById(boardId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }
}
