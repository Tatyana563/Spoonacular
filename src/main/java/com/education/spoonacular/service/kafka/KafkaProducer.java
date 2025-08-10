package com.education.spoonacular.service.kafka;

import com.education.spoonacular.dto.fetch.RecipeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, RecipeDto> kafkaTemplate;
    private final String topic;

    public KafkaProducer(KafkaTemplate<String, RecipeDto> kafkaTemplate,
                         @Value("${spring.kafka.app.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendRecipes(List<RecipeDto> recipes) {
        for (RecipeDto recipe : recipes) {
            kafkaTemplate.send(topic, recipe.getUrl(), recipe);
        }
    }
}
