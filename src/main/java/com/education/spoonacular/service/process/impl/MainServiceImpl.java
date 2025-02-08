package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.service.process.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class MainServiceImpl implements MainService {
    private final NutrientService nutrientService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    private final RecipeNutrientService recipeNutrientService;

    public void processResponse(List<RecipeDto> recipeDtos) {
        List<RecipeDto> filteredRecipeDtos = filterRecipiesWithFaultIngredients(recipeDtos);
        List.of(cuisineService, nutrientService, ingredientService,recipeService,recipeNutrientService)
                .forEach(generalService -> generalService.collectAndSaveNewEntities(filteredRecipeDtos));
    }

    private List<RecipeDto> filterRecipiesWithFaultIngredients(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .filter(recipeDto -> recipeDto.getNutritionDto() != null &&
                        recipeDto.getNutritionDto().getRecipeIngredientDto() != null &&
                        recipeDto.getNutritionDto().getRecipeIngredientDto().stream()
                                .allMatch(dto -> dto.getUnit() != null && !dto.getUnit().isEmpty()&& dto.getAmount() != null))
                .collect(Collectors.toList());
    }
}
