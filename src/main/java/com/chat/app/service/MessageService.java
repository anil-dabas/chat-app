package com.chat.app.service;

import com.chat.app.exception.MessageDoesNotExist;
import com.chat.app.model.Message;

import java.util.List;

public interface MessageService {

    Message getMessageById(String id) throws MessageDoesNotExist;
    Message getAllUnreadMessage();

    Message saveChatMessage(Message chatMessage);

    List<Message> getAllMessage();
}
