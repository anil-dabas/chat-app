package com.chat.app.controller.api;

import com.chat.app.exception.UserNotSubscribed;
import com.chat.app.model.Message;
import com.chat.app.room.ChatRoom;
import com.chat.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatRoom chatRoom;
    private final MessageService messageService;

    @Autowired
    public ChatController(ChatRoom chatRoom,MessageService messageService){
        this.chatRoom = chatRoom;
        this.messageService = messageService;
    }

/*    @GetMapping()
    public void websocket(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println("I am here ");
    }*/

    @PostMapping("/send")
    public void sendMessage(@RequestBody() String message, @AuthenticationPrincipal UserDetails userDetails) throws UserNotSubscribed {
        chatRoom.addMessage(message,userDetails.getUsername());
    }

    @GetMapping("/receive")
    public List<Message> getMessages(@AuthenticationPrincipal UserDetails userDetails) throws UserNotSubscribed {
        return chatRoom.getMessages(userDetails.getUsername());
    }

    @GetMapping("/history")
    public List<Message> getHistoryMessages(){
        return messageService.getAllMessage();
    }

    @PostMapping("/subscribe")
    public void subscribeChatRoom(@RequestBody() Boolean subscribe, @AuthenticationPrincipal UserDetails userDetails){
        chatRoom.subscribeChatRoom(userDetails.getUsername(),subscribe);
    }
}
