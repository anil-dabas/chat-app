package com.chat.app.config;

import com.chat.app.controller.ws.WSChatController;
import com.chat.app.room.WsChatRoom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WsChatRoom chatRoom;
    private final UserDetailsService userDetailsService;

    public WebSocketConfig(WsChatRoom chatRoom, UserDetailsService userDetailsService){
        this.chatRoom = chatRoom;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWsController(),"/chat").setAllowedOrigins("*")
                .addInterceptors(webSocketAuthInterceptor());
    }

    @Bean
    public WebSocketHandler chatWsController() {
        return new WSChatController(chatRoom);
    }

    @Bean
    public WebSocketAuthInterceptor webSocketAuthInterceptor() {
        return new WebSocketAuthInterceptor(userDetailsService);
    }

}
