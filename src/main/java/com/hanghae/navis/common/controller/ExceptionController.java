package com.hanghae.navis.common.controller;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static com.hanghae.navis.common.entity.ExceptionMessage.DUPLICATE_RESOURCE;


@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Message> handleDataException() {
        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return Message.toExceptionResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<Message> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getExceptionMessage());
        return Message.toExceptionResponseEntity(e.getExceptionMessage());
    }

    //정규식
    @ExceptionHandler({BindException.class})
    public ResponseEntity bindException(BindException ex) {
        return new Message().toAllExceptionResponseEntity(HttpStatus.BAD_REQUEST, ex.getFieldError().getDefaultMessage(), ex.getBindingResult().getTarget());
    }

    //마이리스트 토큰 없을시
    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity missingRequestHeaderException(MissingRequestHeaderException ex) {
        return new Message().toAllExceptionResponseEntity(HttpStatus.BAD_REQUEST, "로그인이 되어있지 않습니다.", null);
    }
    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity handleAll(final Exception ex) {
        return new Message().toAllExceptionResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.toString());
    }
}
