package com.study.springSecurity.controller;

import com.study.springSecurity.exception.ValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

// 특정 예외를 관찰하고 있다가 예외가 발생하면 예외를 가져감
// 컴포넌트이기 때문에 IOC안에서 발생한 예외만 낚아챌 수 있다. (ex. Dto에서 발생한 에러는 낚아챌 수 없다.)
@RestControllerAdvice
public class ExceptionControllerAdvice {

    // ExceptionHandler가 ValidException을 관찰하고 있다가 에러가 발생하면 이 메서드가 실행됨(원래 컨트롤러는 실행되지 않음)
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<?> validException(ValidException e) {
        return ResponseEntity.badRequest().body(e.getFieldErrors());
    }

    // id가 틀렸을 때 발생하는 예외
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage())));
    }

    // pw가 틀렸을 때 발생하는 예외
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException e) {
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage())));
    }
}