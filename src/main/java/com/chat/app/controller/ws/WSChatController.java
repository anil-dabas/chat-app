package com.chat.app.controller.ws;

import com.chat.app.room.WsChatRoom;
import com.chat.app.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

import static com.chat.app.util.ChatUtil.getUserName;


public class WSChatController extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();
    private final WsChatRoom chatRoom;

    @Autowired
    private ChatRoomService chatRoomService;

    public WSChatController(WsChatRoom chatRoom){
        this.chatRoom = chatRoom;

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
        String userName = getUserName(session);
        if(chatRoom.isMemberOfTheChatRoom(userName)){
            chatRoom.updateSessionInChatRoom(userName,session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userName = getUserName(session);
        if(chatRoom.isMemberOfTheChatRoom(userName)){
            chatRoom.updateSessionInChatRoom(userName,null);
        }
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        chatRoomService.handleChatFunctions(session, payload);
    }
}
