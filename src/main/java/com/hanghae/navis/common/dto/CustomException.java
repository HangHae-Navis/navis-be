package com.hanghae.navis.common.dto;

import com.hanghae.navis.common.entity.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ExceptionMessage exceptionMessage;
}
