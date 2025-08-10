package com.education.spoonacular.listener;

import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.service.process.impl.MainServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SpoonacularKafkaListener {
    private final String topic;
    private final MainServiceImpl mainService;
    private final ObjectMapper objectMapper;
    public SpoonacularKafkaListener(@Value("${spring.kafka.app.topic}") String topic, MainServiceImpl mainService, ObjectMapper objectMapper) {
        this.topic = topic;
        this.mainService = mainService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.app.topic}")
    public void listen(String message) throws JsonProcessingException {
        ArrayList<RecipeDto> list = new ArrayList();
        try {
           RecipeDto recipe = objectMapper.readValue(
                    message, new TypeReference<RecipeDto>() {}
           );
            list.add(recipe);

            mainService.processData(list);
        } catch (JsonProcessingException e) {
          log.error("Unable to read from Kafka",e.getMessage());
        }
    }
}


