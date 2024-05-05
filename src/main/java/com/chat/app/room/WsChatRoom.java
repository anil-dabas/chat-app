package com.chat.app.room;


import com.chat.app.domainvalue.Action;
import com.chat.app.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

import static com.chat.app.domainvalue.Action.JOIN;
import static com.chat.app.domainvalue.Action.LEAVE;
import static com.chat.app.util.ChatUtil.getUserName;

@Component
public class WsChatRoom {

    private final Map<String, Message> chatMessages = new LinkedHashMap<>();
    private final Map<String,WebSocketSession> chatRoomUsers = new HashMap<>();


    public void updateSessionInChatRoom(String name, WebSocketSession webSocketSession){
        chatRoomUsers.put(name,webSocketSession);
    }

    public Set<WebSocketSession> getAllChatRoomMembersExcept(String user) {
        return chatRoomUsers.entrySet().stream().filter(entry -> !entry.getKey().equalsIgnoreCase(user)).filter(entry -> entry.getValue() != null).map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public boolean isMemberOfTheChatRoom(String name){
        return chatRoomUsers.containsKey(name);
    }

    public void handleChatRoomSubscription(Action action, WebSocketSession webSocketSession) {
        if(JOIN.equals(action)){
            chatRoomUsers.put(getUserName(webSocketSession),webSocketSession);
        }else if(LEAVE.equals(action)){
            chatRoomUsers.remove(getUserName(webSocketSession));
        }
    }

    public void addMessageToRoom(Message chatMessage) {
        chatMessages.put(chatMessage.getId(), chatMessage);
    }

    public List<Message> fetchMessagesFromChatRoom(){
        return chatMessages.values().stream().toList();
    }

    public void deleteMessageFromRoom(String messageId) {
        chatMessages.remove(messageId);
    }
}
