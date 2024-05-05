package com.chat.app.room;

import com.chat.app.Action;
import com.chat.app.model.Message;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.chat.app.Action.*;

@Component
public class WsChatRoom {

    private final Map<String, Message> chatMessages = new LinkedHashMap<>();
    private final Map<String,WebSocketSession> chatRoomUsers = new ConcurrentHashMap<>();


    public void updateSessionInChatRoom(String name, WebSocketSession webSocketSession){
        chatRoomUsers.put(name,webSocketSession);
    }

    public Set<WebSocketSession> getAllChatRoomMembers(){
        return new HashSet<>(chatRoomUsers.values());
    }

    public Set<WebSocketSession> getAllChatRoomMembersExcept(String user) {
        return chatRoomUsers.entrySet().stream().filter(entry -> !entry.getKey().equalsIgnoreCase(user)).filter(entry -> entry.getValue() != null).map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public boolean isMemberOfTheChatRoom(String name){
        return chatRoomUsers.containsKey(name);
    }

    public void handleChatRoomSubscription(Action action, WebSocketSession webSocketSession) {
        if(JOIN.equals(action)){
            chatRoomUsers.put(Objects.requireNonNull(webSocketSession.getPrincipal()).getName(),webSocketSession);
        }else if(LEAVE.equals(action)){
            chatRoomUsers.remove(Objects.requireNonNull(webSocketSession.getPrincipal()).getName());
        }
    }
}
