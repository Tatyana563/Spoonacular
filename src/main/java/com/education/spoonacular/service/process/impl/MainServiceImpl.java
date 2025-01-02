package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.service.process.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MainServiceImpl implements MainService {
    private final NutrientService nutrientService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public void processResponse(List<RecipeDto> recipeDtos) {

        List.of(cuisineService, nutrientService, ingredientService,recipeService)
                .forEach(generalService -> generalService.collectAndSaveNewEntities(recipeDtos));
    }

}
