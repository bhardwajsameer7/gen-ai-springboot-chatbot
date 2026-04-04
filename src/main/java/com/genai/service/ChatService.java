package com.genai.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
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

    // Store memory per user
    private final Map<String, MessageWindowChatMemory> userMemories = new ConcurrentHashMap<>();

    @Autowired
    public ChatService(@Value("${openrouter.base-url}") String url, @Value("${openrouter.api-key}") String key, @Value("${openrouter.model}") String model){
        this.openAiChatModel = OpenAiChatModel.builder().apiKey(key).baseUrl(url).modelName(model).build();
    }

  
    public String chat(String userId,String message) {

        //Create in-memory to store both AI and User conversion
        MessageWindowChatMemory memory = userMemories.computeIfAbsent(userId, id -> MessageWindowChatMemory.withMaxMessages(10));

        //Add user message
        memory.add(UserMessage.from(message));

        //Send FULL conversation to model
        List<ChatMessage> messages = memory.messages();

        //Get AI response
        AiMessage aiMessage = openAiChatModel.generate(messages).content();

        //Store AI response
        memory.add(aiMessage);

        return aiMessage.text();
    }

}
