package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.service.process.api.CuisineService;
import com.education.spoonacular.service.process.api.MainService;
import com.education.spoonacular.service.process.api.NutrientService;
import com.education.spoonacular.service.process.api.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MainServiceImpl implements MainService {
    private final NutrientService nutrientService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;

    public void processResponse(List<RecipeDto> recipeDtos) {

        List.of( cuisineService, nutrientService,recipeService)
                .forEach(generalService -> generalService.collectAndSaveNewEntities(recipeDtos));
    }

}
