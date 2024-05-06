package com.chat.app.service;

import com.chat.app.model.Message;

import java.util.List;

public interface MessageService {

    Message saveChatMessage(Message chatMessage);

    List<Message> getAllMessage();
}
