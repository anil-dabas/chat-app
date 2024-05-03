package com.chat.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason ="The Requested user has not subscribed to the chat room, please subscribe to the chat room first")
public class UserNotSubscribed extends Exception {
    public UserNotSubscribed(String message) {
        super(message);
    }
}
