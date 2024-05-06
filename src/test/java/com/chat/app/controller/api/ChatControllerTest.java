package com.chat.app.controller.api;

import com.chat.app.config.SecurityConfiguration;
import com.chat.app.room.ChatRoom;
import com.chat.app.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.chat.app.constant.TestConstants.MESSAGE_CONTENT;
import static com.chat.app.constant.TestConstants.TEST_USER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ChatController.class)
@Import(SecurityConfiguration.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoom chatRoom;

    @MockBean
    private MessageService messageService;


    @Test
    @WithMockUser(username = TEST_USER)
    public void testSendMessage() throws Exception {

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MESSAGE_CONTENT))
                .andExpect(status().isOk());

        // Verify that the chatRoom.addMessage method was called with the correct arguments
        verify(chatRoom).addMessage(eq(MESSAGE_CONTENT), eq(TEST_USER));
    }

    @Test
    @WithMockUser(username = TEST_USER)
    public void testSubscribeChatRoom() throws Exception {
        mockMvc.perform(post("/chat/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = TEST_USER)
    public void testGetHistoryMessages() throws Exception {
        mockMvc.perform(get("/chat/history"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}