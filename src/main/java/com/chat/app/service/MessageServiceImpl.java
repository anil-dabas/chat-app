package com.chat.app.service;

import com.chat.app.model.Message;
import com.chat.app.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message saveChatMessage(Message chatMessage) {
        return messageRepository.save(chatMessage);
    }

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }
}
