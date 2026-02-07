package com.education.spoonacular.service.search;

import com.education.spoonacular.config.SpoonProperties;
import com.education.spoonacular.dto.fetch.ResponseDto;
import com.education.spoonacular.service.SpoonacularClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpoonSearchServiceImpl implements SpoonSearchService {
    private final SpoonacularClient spoonacularClient;
    private final SpoonProperties properties;

    @Override
    public ResponseDto getDataByDishAndAmount(String dish, int amount) {
        return spoonacularClient.getDataByDishAndAmount(
                dish,
                amount,
                true,
                true,
                properties.getApiKey()
        );
    }
}
