package com.chat.app.service;
import com.chat.app.domainvalue.Action;
import com.chat.app.model.Message;
import com.chat.app.room.WsChatRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.chat.app.constant.ChatConstants.MESSAGE_ID;
import static com.chat.app.domainvalue.ChatFunction.ACTION;
import static com.chat.app.domainvalue.ChatFunction.MESSAGE;
import static com.chat.app.util.ChatUtil.getUserName;

@Service
public class ChatRoomService {

    private final WsChatRoom chatRoom;
    private final MessageService messageService;
    private final ObjectMapper mapper;

    @Autowired
    public ChatRoomService(WsChatRoom chatRoom,MessageService messageService,ObjectMapper mapper){
        this.chatRoom = chatRoom;
        this.messageService = messageService;
        this.mapper = mapper;
    }

    public void handleChatFunctions(WebSocketSession session, String payload) {
        JsonObject jsonMessage = JsonParser.parseString(payload).getAsJsonObject();
        if(jsonMessage.has(ACTION.name())){
            handleChatActions(session, jsonMessage);
        }
        else if (jsonMessage.has(MESSAGE.name()) && chatRoom.isMemberOfTheChatRoom(getUserName(session))) {
            handleChatMessage(session, jsonMessage.get(MESSAGE.name()).getAsString());
        }
    }


    private void handleChatActions(WebSocketSession session, JsonObject jsonObject) {
        Action action = Action.fromActionString(jsonObject.get(ACTION.name()).getAsString());
        String userName = getUserName(session);
        switch (action) {
            case JOIN, LEAVE -> chatRoom.handleChatRoomSubscription(action, session);
            case FETCH -> {
                if (chatRoom.isMemberOfTheChatRoom(userName)) {
                    fetchAndSendToUser(session);
                }
            }
            case DELETE -> {
                if (chatRoom.isMemberOfTheChatRoom(userName) && jsonObject.has(MESSAGE_ID)) {
                    deleteMessageFromChatRoom(jsonObject.get(MESSAGE_ID).getAsString());
                }
            }
            case HISTORY -> {
                if (chatRoom.isMemberOfTheChatRoom(userName)) {
                    fetchFromDatabaseAndSend(session);
                }
            }
        }
    }

    private void deleteMessageFromChatRoom(String messageId) {
        chatRoom.deleteMessageFromRoom(messageId);
    }


    private void handleChatMessage(WebSocketSession session, String message){
        String chatMessageString = getProcessedAndFormattedChatMessage(getUserName(session), message);
        chatRoom.getAllChatRoomMembersExcept(getUserName(session)).forEach(wsSession -> {
            try {
                wsSession.sendMessage(new TextMessage(chatMessageString));  // getUserName(session) +" : "+message
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void fetchFromDatabaseAndSend(WebSocketSession session) {
        List<Message> messages = messageService.getAllMessage();
        sendMessagesToSingleUser(session, messages);
    }


    private void fetchAndSendToUser(WebSocketSession session) {
        List<Message> messages = chatRoom.fetchMessagesFromChatRoom();
        sendMessagesToSingleUser(session,messages);

    }

    private void sendMessagesToSingleUser(WebSocketSession session, List<Message> messages) {
        messages.forEach(message -> {
            try {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getProcessedAndFormattedChatMessage(String user, String message) {
        Message chatMessage = Message.builder().content(message).sender(user).timestamp(LocalDateTime.now()).build();
        chatMessage = messageService.saveChatMessage(chatMessage);
        chatRoom.addMessageToRoom(chatMessage);
        String chatMessageString = null;
        try {
            chatMessageString = mapper.writeValueAsString(chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return chatMessageString;
    }
}
