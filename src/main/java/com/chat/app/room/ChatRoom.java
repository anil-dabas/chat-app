package com.chat.app.room;

import com.chat.app.exception.UserNotSubscribed;
import com.chat.app.model.Message;
import com.chat.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ChatRoom {

    @Autowired
    private MessageService messageService;

    private final Map<String,Message> chatMessages = new LinkedHashMap<>();
    private final Set<String> chatRoomUsers = new HashSet<>();

    public void addMessage(String message, String user) throws UserNotSubscribed {
        if(chatRoomUsers.contains(user)){
            Message chatMessage = Message.builder().content(message).sender(user).timestamp(LocalDateTime.now()).build();
            chatMessage = messageService.saveChatMessage(chatMessage);
            chatMessages.put(chatMessage.getId(), chatMessage);
        }else
            throw new UserNotSubscribed("The user has not subscribed to the chat room please subscribe to the chat room first");
    }

    public List<Message> getMessages(String user) throws UserNotSubscribed {
        if(chatRoomUsers.contains(user))
            return chatMessages.values().stream().toList();
        else
            throw new UserNotSubscribed("The user has not subscribed to the chat room please subscribe to the chat room first");
    }

    public void deleteMessage(String id){
        chatMessages.remove(id);
    }

    public void subscribeChatRoom(String user, boolean subscribe){
        if(subscribe)
            chatRoomUsers.add(user);
        else
            chatRoomUsers.remove(user);
    }

}
