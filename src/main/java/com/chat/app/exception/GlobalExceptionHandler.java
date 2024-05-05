package com.chat.app.exception;

import com.chat.app.model.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MessageDoesNotExist.class, UserNotSubscribed.class})
    public ResponseEntity<ExceptionBody> handleAllException(Exception exception){
        ExceptionBody exceptionBody = ExceptionBody.builder().status(HttpStatus.BAD_REQUEST).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }
}
