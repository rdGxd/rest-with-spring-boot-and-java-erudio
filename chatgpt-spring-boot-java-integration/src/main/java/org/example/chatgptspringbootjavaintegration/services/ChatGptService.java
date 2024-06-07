package org.example.chatgptspringbootjavaintegration.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chatgptspringbootjavaintegration.vo.request.ChatGptRequest;
import org.example.chatgptspringbootjavaintegration.vo.response.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class ChatGptService {
    private final Logger logger = Logger.getLogger(ChatGptService.class.getName());

    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String url;

    @Autowired
    private RestTemplate template;


    public Object chat(String prompt) {
        logger.info("Starting Prompt");
        ChatGptRequest request = new ChatGptRequest(model, prompt);
       //  String json = new ObjectMapper().writeValueAsString(request);
        // logger.info(json);
        logger.info("Processing Prompt");
        ChatGptResponse response = template.postForObject(url, request, ChatGptResponse.class);
        return response.getChoices().getFirst().getMessage().getContent();
    }
}
