package com.chat.app.util;

import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

public class ChatUtil {
    public static String getUserName(WebSocketSession session) {
        return Objects.requireNonNull(session.getPrincipal()).getName();
    }
}
