package com.education.spoonacular.service.search;

import com.education.spoonacular.config.SpoonProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SpoonSearchServiceImpl implements SpoonSearchProcess {
    private final RestTemplate restTemplate;
    private final SpoonProperties properties;

    @Override
    public String getDataByDishAndAmount(String dish, int amount) {
        String url = properties.getBaseUrl() + dish + "&number=" + amount + "&addRecipeInformation=true&addRecipeNutrition=true&apiKey=" + properties.getApiKey();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        writeToFile(body);
        return body;
    }

    private void writeToFile(String input){
        try (FileWriter writer = new FileWriter("output.json")) {
            writer.write(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
