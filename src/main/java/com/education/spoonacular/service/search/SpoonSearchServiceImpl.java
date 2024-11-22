package com.education.spoonacular.service.search;

import com.education.spoonacular.config.SpoonProperties;
import com.education.spoonacular.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class SpoonSearchServiceImpl implements SpoonSearchProcess {
    private final RestTemplate restTemplate;
    private final SpoonProperties properties;

    @Override
    public ResponseDto getDataByDishAndAmount(String dish, int amount) {
        String url = String.format("%s%s&number=%d&addRecipeInformation=true&addRecipeNutrition=true&apiKey=%s",
                properties.getBaseUrl(),
                dish,
                amount,
                properties.getApiKey());
        ResponseEntity<ResponseDto> response = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<>() {
        });

        return response.getBody();


    }

    private void writeToFile(String input) {
        try (FileWriter writer = new FileWriter("output.json")) {
            writer.write(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
