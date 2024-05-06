package com.chat.app.constant;

import com.chat.app.model.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestConstants {
    public static final String TEST_USER = "user1";
    public static final String TEST_USER_2 = "user2";
    public static final String MESSAGE_CONTENT = "Hello!";
    public static final String ACTION = "ACTION";
    public static final String JOIN_ACTION = "join";
    public static final String LEAVE_ACTION = "leave";
    public static final String FETCH_ACTION = "fetch";
    public static final String HISTORY_ACTION = "history";
    public static final String DELETE_ACTION = "delete";

    public static Message prepareMessage() {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .content(MESSAGE_CONTENT)
                .sender(TEST_USER)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
