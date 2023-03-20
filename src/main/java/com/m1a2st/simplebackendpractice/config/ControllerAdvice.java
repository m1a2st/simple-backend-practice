package com.m1a2st.simplebackendpractice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author m1a2st
 * @Date 2023/3/20
 * @Version v1.0
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> globalExceptionHandler(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String globalRuntimeExceptionHandler(Exception e){
        log.error(e.getMessage());
        return e.getMessage();
    }
}
