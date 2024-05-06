package com.chat.app.service;

import com.chat.app.model.Message;
import com.chat.app.repo.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.ArrayList;
import java.util.List;

import static com.chat.app.constant.TestConstants.prepareMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageServiceImplTest {

    @Autowired
    private MessageServiceImpl messageService;

    @MockBean
    private MessageRepository messageRepository;


    @Test
    public void testSaveChatMessage() {
        // Prepare
        Message mockedMessage = prepareMessage();
        when(messageRepository.save(any(Message.class))).thenReturn(mockedMessage);

        // Call
        Message savedMessage = messageService.saveChatMessage(mockedMessage);

        // Assert
        verify(messageRepository, times(1)).save(mockedMessage);
        assertEquals(mockedMessage,savedMessage);
    }

    @Test
    public void testGetAllMessages() {

        // Prepare
        List<Message> mockedMessages = new ArrayList<>();
        mockedMessages.add(prepareMessage());
        mockedMessages.add(prepareMessage());
        mockedMessages.add(prepareMessage());
        when(messageRepository.findAll()).thenReturn(mockedMessages);

        // Call
        List<Message> returnedMessages = messageService.getAllMessage();

        // Assert
        verify(messageRepository, times(1)).findAll();
        assertEquals(mockedMessages, returnedMessages);
    }

    @Test
    public void testGetAllMessagesWhenNoMessage() {

        // Prepare
        when(messageRepository.findAll()).thenReturn(List.of());

        // Call
        List<Message> returnedMessages = messageService.getAllMessage();

        // Assert
        verify(messageRepository, times(1)).findAll();
    }
}