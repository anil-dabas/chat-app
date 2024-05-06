package com.chat.app.room;

import static com.chat.app.constant.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import com.chat.app.exception.UserNotSubscribed;
import com.chat.app.model.Message;
import com.chat.app.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ChatRoomTest {

    @MockBean
    private MessageService messageService;

    @Autowired
    private ChatRoom chatRoom;

    @BeforeEach
    public void setup(){
        chatRoom.subscribeChatRoom(TEST_USER,false);
    }
    @Test
    public void testAddMessageWithSubscribedUser() throws UserNotSubscribed {
        Message savedMessage = prepareMessage();
        when(messageService.saveChatMessage(any(Message.class))).thenReturn(savedMessage);

        chatRoom.subscribeChatRoom(TEST_USER, true);
        chatRoom.addMessage(MESSAGE_CONTENT, TEST_USER);

        assertEquals(1, chatRoom.getMessages(TEST_USER).size());
        assertEquals(MESSAGE_CONTENT, chatRoom.getMessages(TEST_USER).get(0).getContent());
        assertEquals(TEST_USER, chatRoom.getMessages(TEST_USER).get(0).getSender());
    }


    @Test
    public void testAddMessageWithUnsubscribedUser(){

        Message savedMessage = prepareMessage();
        when(messageService.saveChatMessage(any(Message.class))).thenReturn(savedMessage);
        assertThrows(UserNotSubscribed.class, () -> chatRoom.addMessage(MESSAGE_CONTENT, TEST_USER));

    }

    @Test
    public void testGetMessages() {
        assertThrows(UserNotSubscribed.class, () -> chatRoom.getMessages(TEST_USER));
    }

    @Test
    public void testDeleteMessage() throws UserNotSubscribed {
        Message savedMessage = prepareMessage();
        when(messageService.saveChatMessage(any(Message.class))).thenReturn(savedMessage);

        chatRoom.subscribeChatRoom(TEST_USER, true);
        chatRoom.addMessage(MESSAGE_CONTENT, TEST_USER);

        // Checking that the message is added successfully
        assertEquals(1, chatRoom.getMessages(TEST_USER).size());
        String messageId = chatRoom.getMessages(TEST_USER).get(0).getId();
        chatRoom.deleteMessage(messageId);
        assertEquals(0, chatRoom.getMessages(TEST_USER).size());
    }
}
