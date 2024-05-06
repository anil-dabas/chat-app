package com.chat.app.controller.ws;

import com.chat.app.room.WsChatRoom;
import com.chat.app.service.ChatRoomService;
import com.chat.app.util.ChatUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import static com.chat.app.constant.TestConstants.TEST_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WSChatControllerTest {

    private WSChatController wsChatController;

    @Mock
    private WsChatRoom mockChatRoom;

    private ChatRoomService mockChatRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        wsChatController = new WSChatController(mockChatRoom);
        mockChatRoomService = mock(ChatRoomService.class);
    }

    @AfterEach
    void tearDown() {
        // Reset the static mock after each test
        framework().clearInlineMocks();
    }

    MockedStatic<ChatUtil> chatUtilMockedStatic = mockStatic(ChatUtil.class);
    @Test
    void testAfterConnectionEstablished() {
        // Mock
        WebSocketSession mockSession = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(mockSession)).thenReturn(TEST_USER);
        when(mockSession.getId()).thenReturn("session1");
        when(mockChatRoom.isMemberOfTheChatRoom(TEST_USER)).thenReturn(true);

        // Call
        wsChatController.afterConnectionEstablished(mockSession);

        // Verify
        verify(mockChatRoom).updateSessionInChatRoom(TEST_USER, mockSession);
    }

    @Test
    void testAfterConnectionClosed() {
        // Mock
        WebSocketSession mockSession = mock(WebSocketSession.class);
        chatUtilMockedStatic.when(() -> ChatUtil.getUserName(mockSession)).thenReturn(TEST_USER);
        when(mockSession.getId()).thenReturn("session1");
        when(mockChatRoom.isMemberOfTheChatRoom(TEST_USER)).thenReturn(true);

        // Call
        wsChatController.afterConnectionClosed(mockSession, CloseStatus.NORMAL);

        // Verify
        verify(mockChatRoom).updateSessionInChatRoom(TEST_USER, null);
    }

}