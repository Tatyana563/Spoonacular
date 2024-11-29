package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MainServiceImpl {
    private final NutrientService nutrientService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;

    public void processResponse(List<RecipeDto> recipeDtos) {
        nutrientService.collectAndSaveNewEntities(recipeDtos);

//TODO: do the same as with Recipes, Ingredients, 4 entities have the same logic - use abstract class
        cuisineService.collectAndSaveNewEntities(recipeDtos);

        recipeService.collectAndSaveNewEntities(recipeDtos);

    }
}
