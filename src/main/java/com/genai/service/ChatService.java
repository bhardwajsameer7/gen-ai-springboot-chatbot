package com.genai.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final OpenAiChatModel openAiChatModel;

    // Store Assistant per user (important for memory isolation)
    private final Map<String, Assistant> assistants = new ConcurrentHashMap<>();

    @Autowired
    public ChatService(OpenAiChatModel openAiChatModel){
        this.openAiChatModel = openAiChatModel;
    }

    public String chat(String userId,String message) {

        //Create Assistant per user (with memory)
        Assistant assistant = assistants.computeIfAbsent(userId, id ->
                AiServices.builder(Assistant.class)
                        .chatLanguageModel(openAiChatModel)
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                        .build()
        );

        //Call assistant (system prompt + memory automatically handled)
        return assistant.chat(message);
    }

}
