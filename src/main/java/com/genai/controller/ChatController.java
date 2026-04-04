package com.genai.controller;

import com.genai.dto.ChatResponse;
import com.genai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestHeader(value = "X-User-Id", required = false) String userId, @RequestBody String message){

        // Generate UUID if not provided
        if (userId == null || userId.isEmpty()) {
            userId = UUID.randomUUID().toString();
        }

        String response = chatService.chat(userId, message);

        return ResponseEntity.ok(new ChatResponse(userId, response));
    }
}
