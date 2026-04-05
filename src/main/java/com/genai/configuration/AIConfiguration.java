package com.genai.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AIConfiguration {

    @Bean
    public OpenAiChatModel openAiChatModel(@Value("${openrouter.base-url}") String url, @Value("${openrouter.api-key}") String key, @Value("${openrouter.model}") String model){
        return OpenAiChatModel.builder().apiKey(key).baseUrl(url).modelName(model).build();
    }
    
}