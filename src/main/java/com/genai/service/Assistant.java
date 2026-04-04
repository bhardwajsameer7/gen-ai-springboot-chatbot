package com.genai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {

    @SystemMessage("You are a helpful AI teacher.Always be concise,accurate & in roman bullet points ")
    String chat(@UserMessage String message);
}