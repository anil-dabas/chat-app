package com.chat.app.service;

import com.chat.app.model.Message;
import com.chat.app.room.WsChatRoom;
import com.chat.app.util.ChatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.chat.app.constant.ChatConstants.MESSAGE_ID;
import static com.chat.app.constant.TestConstants.*;
import static com.chat.app.domainvalue.Action.JOIN;
import static com.chat.app.domainvalue.Action.LEAVE;
import static org.mockito.Mockito.*;

@SpringBootTest
class ChatRoomServiceTest {


    public static final String MESSAGE = "MESSAGE";
    private ChatRoomService chatRoomService;

    private WsChatRoom wsChatRoom;

    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    MockedStatic<ChatUtil> chatUtilMockedStatic = mockStatic(ChatUtil.class);

    @BeforeEach
    void setUp(){
        wsChatRoom = mock(WsChatRoom.class);
        messageService = mock(MessageService.class);
        chatRoomService = new ChatRoomService(wsChatRoom,messageService,objectMapper);
    }

    @AfterEach
    void tearDown() {
        // Reset the static mock after each test
        framework().clearInlineMocks();
    }

    @Test
    void testHandleChatFunctionsHandleChatActionsJoin() {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session)).thenReturn(TEST_USER);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACTION, JOIN_ACTION);

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());
        verify(wsChatRoom).handleChatRoomSubscription(JOIN,session);
    }

    @Test
    void testHandleChatFunctionsHandleChatActionsLeave() {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session)).thenReturn(TEST_USER);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACTION, LEAVE_ACTION);

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());

        // Verify
        verify(wsChatRoom).handleChatRoomSubscription(LEAVE, session);
    }

    @Test
    void testHandleChatFunctionsHandleChatActionsFetch() {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACTION, FETCH_ACTION);
        when(wsChatRoom.isMemberOfTheChatRoom(any())).thenReturn(true);

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());

        // Verify
        verify(wsChatRoom).fetchMessagesFromChatRoom();
    }

    @Test
    void testHandleChatFunctionsHandleChatActionsHistory() throws IOException {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACTION, HISTORY_ACTION);
        when(wsChatRoom.isMemberOfTheChatRoom(any())).thenReturn(true);
        List<Message> messages = List.of(prepareMessage());
        when(messageService.getAllMessage()).thenReturn(messages);

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());

        // Verify
        verify(messageService).getAllMessage();
        verify(session).sendMessage(any(TextMessage.class));


    }

    @Test
    void testHandleChatFunctions_HandleChatMessage() throws IOException {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(session)).thenReturn(TEST_USER);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(MESSAGE, MESSAGE_CONTENT);
        when(wsChatRoom.isMemberOfTheChatRoom(any())).thenReturn(true);
        Set<WebSocketSession> sessions = new HashSet<>();
        sessions.add(session);
        when(wsChatRoom.getAllChatRoomMembersExcept(anyString())).thenReturn(sessions);
        when(messageService.saveChatMessage(any(Message.class))).thenReturn(prepareMessage());

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());

        // Verify
        verify(session).sendMessage(any(TextMessage.class));
    }

    @Test
    void testHandleChatFunctionsHandleChatActionsDelete() throws IOException {
        // Mock
        WebSocketSession session = mock(WebSocketSession.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACTION, DELETE_ACTION);
        Message message = prepareMessage();
        List<Message> messages = List.of(message);
        jsonObject.addProperty(MESSAGE_ID,message.getId());
        when(wsChatRoom.isMemberOfTheChatRoom(any())).thenReturn(true);
        when(messageService.getAllMessage()).thenReturn(messages);

        // Call
        chatRoomService.handleChatFunctions(session, jsonObject.toString());

        // Verify
        verify(wsChatRoom).deleteMessageFromRoom(message.getId());
    }
}