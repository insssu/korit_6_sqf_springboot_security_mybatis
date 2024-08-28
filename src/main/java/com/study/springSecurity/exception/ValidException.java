package com.study.springSecurity.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidException extends RuntimeException {

    @Getter
    private List<FieldError> fieldErrors;

    public ValidException(String message, List<FieldError> fieldErrors) {
        super(message);  // 예외발생 시 출력한 메시지를 무조건 담아줘야함
        this.fieldErrors = fieldErrors;
    }
}
