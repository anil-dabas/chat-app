package com.chat.app.room;

import com.chat.app.domainvalue.Action;
import com.chat.app.model.Message;
import com.chat.app.util.ChatUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;

import static com.chat.app.constant.TestConstants.*;
import static com.chat.app.domainvalue.Action.JOIN;
import static com.chat.app.domainvalue.Action.LEAVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WsChatRoomTest {

    private  WsChatRoom wsChatRoom;

    @BeforeEach
    void setUp(){
        wsChatRoom = new WsChatRoom();
    }
    @AfterEach
    void tearDown() {
        // Reset the static mock after each test
        framework().clearInlineMocks();
    }

    MockedStatic<ChatUtil> chatUtilMockedStatic = mockStatic(ChatUtil.class);

    @Test
    public void testUpdateSessionInChatRoom() {
        // Mock
        WebSocketSession session1 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session1)).thenReturn(TEST_USER);
        WebSocketSession session2 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session2)).thenReturn(TEST_USER_2);
        WebSocketSession session3 = mock(WebSocketSession.class);

        // Prepare
        wsChatRoom.handleChatRoomSubscription(JOIN, session1);
        wsChatRoom.handleChatRoomSubscription(JOIN, session2);

        // Call

        wsChatRoom.updateSessionInChatRoom(TEST_USER,session3);
        // Assert
        assertTrue(wsChatRoom.isMemberOfTheChatRoom(TEST_USER));
        assertTrue(wsChatRoom.isMemberOfTheChatRoom(TEST_USER_2));

        Set<WebSocketSession> membersExceptUser2 = wsChatRoom.getAllChatRoomMembersExcept(TEST_USER_2);
        assertTrue(membersExceptUser2.contains(session3));

    }

    @Test
    public void testGetAllChatRoomMembersExcept() {
        // Mock
        WebSocketSession session1 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session1)).thenReturn(TEST_USER);
        WebSocketSession session2 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session2)).thenReturn(TEST_USER_2);
        wsChatRoom.handleChatRoomSubscription(JOIN, session1);
        wsChatRoom.handleChatRoomSubscription(JOIN, session2);

        // Call
        Set<WebSocketSession> membersExceptUser1 = wsChatRoom.getAllChatRoomMembersExcept(TEST_USER);

        // Assert
        assertEquals(1, membersExceptUser1.size());
        assertFalse(membersExceptUser1.contains(session1));

    }



    @Test
    public void testIsMemberOfTheChatRoom() {
        // Mock
        WebSocketSession session1 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session1)).thenReturn(TEST_USER);
        wsChatRoom.handleChatRoomSubscription(JOIN, session1);

        // Assert
        assertTrue(wsChatRoom.isMemberOfTheChatRoom(TEST_USER));
        assertFalse(wsChatRoom.isMemberOfTheChatRoom(TEST_USER_2));

    }

    @Test
    public void testHandleChatRoomSubscription() {
        // Mock
        WebSocketSession session1 = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session1)).thenReturn(TEST_USER);

        // Call
        wsChatRoom.handleChatRoomSubscription(Action.JOIN, session1);

        // Assert
        assertTrue(wsChatRoom.isMemberOfTheChatRoom(TEST_USER));

        // Call
        wsChatRoom.handleChatRoomSubscription(LEAVE, session1);

        // Assert
        assertFalse(wsChatRoom.isMemberOfTheChatRoom(TEST_USER));
    }

    @Test
    public void testAddMessageToChatRoom() {
        // Mock
        Message message = prepareMessage();

        // Call
        wsChatRoom.addMessageToRoom(message);
        List<Message> messages = wsChatRoom.fetchMessagesFromChatRoom();

        // Assert
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));

    }

    @Test
    public void testFetchMessagesFromChatRoom() {
        // Prepare
        Message message1 = prepareMessage();
        Message message2 = prepareMessage();
        wsChatRoom.addMessageToRoom(message1);
        wsChatRoom.addMessageToRoom(message2);

        // Call
        List<Message> messages = wsChatRoom.fetchMessagesFromChatRoom();

        // Assert
        assertEquals(2, messages.size());
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));
    }

    @Test
    public void testDeleteMessageFromRoom() {
        // Prepare
        Message message = prepareMessage();
        wsChatRoom.addMessageToRoom(message);

        // Call
        wsChatRoom.deleteMessageFromRoom(message.getId());
        List<Message> messages = wsChatRoom.fetchMessagesFromChatRoom();

        // Assert
        assertTrue(messages.isEmpty());
    }
}