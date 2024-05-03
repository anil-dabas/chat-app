package com.chat.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason ="The requested message does not exist")
public class MessageDoesNotExist extends Exception{
    public MessageDoesNotExist(String message) {
        super(message);
    }
}
