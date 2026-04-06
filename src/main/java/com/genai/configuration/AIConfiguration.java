package com.genai.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
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

    @Bean
    public EmbeddingStoreContentRetriever embeddingStoreContentRetriever() throws Exception {

        //Read JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("rag.json");

        JsonNode root = mapper.readTree(is);

        List<Document> documents = new ArrayList<>();

        for (JsonNode node : root.get("topics")) {
            String text = node.get("title").asText() + ": " + node.get("content").asText();
            documents.add(Document.from(text));
        }

        //Break document into smaller chunks
        List<TextSegment> segments = DocumentSplitters.recursive(300, 50).splitAll(documents);

        //Embedding model
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        //Store
        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        store.addAll(embeddingModel.embedAll(segments).content(), segments);

        // 5️⃣ Retriever
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .build();
    }
}