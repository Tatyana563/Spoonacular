package com.education.spoonacular.service.search;

import com.education.spoonacular.config.SpoonProperties;
import com.education.spoonacular.dto.fetch.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class SpoonSearchServiceImpl implements SpoonSearchService {
    private final RestTemplate restTemplate;
    private final SpoonProperties properties;

    @Override
    public ResponseDto getDataByDishAndAmount(String dish, int amount) {

        String url = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
                .queryParam("query",dish)
                .queryParam("number", amount)
                .queryParam("addRecipeInformation", true)
                .queryParam("addRecipeNutrition", true)
                .queryParam("apiKey", properties.getApiKey())
                .build()
                .toString();

        ResponseEntity<ResponseDto> response = restTemplate.exchange(url, GET, null, ResponseDto.class);

        return response.getBody();

    }
}
