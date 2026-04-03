package com.genai.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAiChatModel openAiChatModel;

    @Autowired
    public ChatService(@Value("${openrouter.base-url}") String url, @Value("${openrouter.api-key}") String key, @Value("${openrouter.model}") String model){

        this.openAiChatModel = OpenAiChatModel.builder().apiKey(key).baseUrl(url).modelName(model).build();
    }

    public String chat(String message) {
        return openAiChatModel.generate(message);
    }

}
