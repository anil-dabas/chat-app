package com.chat.app.controller.ws;

import com.chat.app.Action;
import com.chat.app.room.WsChatRoom;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.chat.app.Action.*;


public class WSChatController extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();
    private final WsChatRoom chatRoom;

    public WSChatController(WsChatRoom chatRoom){
        this.chatRoom = chatRoom;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
        String userName = Objects.requireNonNull(session.getPrincipal()).getName();
        if(chatRoom.isMemberOfTheChatRoom(userName)){
            chatRoom.updateSessionInChatRoom(userName,session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userName = Objects.requireNonNull(session.getPrincipal()).getName();
        if(chatRoom.isMemberOfTheChatRoom(userName)){
            chatRoom.updateSessionInChatRoom(userName,null);
        }
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        JsonObject jsonMessage = JsonParser.parseString(payload).getAsJsonObject();
        if(jsonMessage.has("action")){
            Action action = fromActionString(jsonMessage.get("action").getAsString());
            if (JOIN.equals(action) || LEAVE.equals(action)){
                chatRoom.handleChatRoomSubscription(action,session);
            }
        }
        else if (jsonMessage.has("message")) {
            handleMessage(session, jsonMessage.get("message").getAsString());
        }
    }


    private void handleMessage(WebSocketSession session, String message){
        chatRoom.getAllChatRoomMembersExcept(Objects.requireNonNull(session.getPrincipal()).getName()).forEach(wsSession -> {
            try {
                wsSession.sendMessage(new TextMessage(session.getPrincipal().getName() +" : "+message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
