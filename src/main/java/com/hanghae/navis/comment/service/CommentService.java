package com.hanghae.navis.comment.service;

import com.amazonaws.Response;
import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.board.entity.Comment;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.comment.dto.CommentRequestDto;
import com.hanghae.navis.comment.dto.CommentResponseDto;
import com.hanghae.navis.comment.repository.CommentRepository;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> commentList(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<CommentResponseDto> responseList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);

        for(Comment comment : commentList) {
            responseList.add(new CommentResponseDto(comment));
        }
        return Message.toResponseEntity(COMMENT_LIST_GET_SUCCESS, responseList);
    }

    @Transactional
    public ResponseEntity<Message> createComment(Long groupId, Long boardId, CommentRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Comment comment = new Comment(requestDto, user, board);

        commentRepository.save(comment);

        return Message.toResponseEntity(COMMENT_POST_SUCCESS, new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<Message> updateComment(Long groupId, Long boardId, Long commentId, CommentRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        if(!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        comment.updateComment(requestDto);

        return Message.toResponseEntity(COMMENT_UPDATE_SUCCESS, new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<Message> deleteComment(Long groupId, Long boardId, Long commentId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        commentRepository.deleteById(commentId);

        return Message.toResponseEntity(COMMENT_DELETE_SUCCESS);
    }
}
