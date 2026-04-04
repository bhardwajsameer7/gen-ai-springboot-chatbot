package com.genai.dto;

import lombok.Getter;
import lombok.Setter;


public class ChatResponse {

    @Getter
    private String userId;

    @Getter
    private String message;

    public ChatResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}