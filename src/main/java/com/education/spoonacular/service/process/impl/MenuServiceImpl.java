package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.dto.NutritionalInfoDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.process.api.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final RecipeRepository recipeRepository;

    public List<DishDto> getSuggestedDishes(LunchRequestDto request) {

        int targetCalories = calculateCalories(request.getGender(), request.getActivityLevel());

        Set<String> cuisine = request.getCuisinePreferences();
        Set<Integer> allergens = request.getIngredientsExclusions();
        List<Recipe> suggestedRecipes = getSuggestedRecipes(cuisine, targetCalories);
        return convertRecipeToDishDtos(suggestedRecipes);
    }
//TODO: mapstruct
    public List<DishDto> convertRecipeToDishDtos(List<Recipe> suggestedRecipes) {
        return suggestedRecipes.stream().map(recipe -> {
            DishDto dishDto = new DishDto();
            dishDto.setName(recipe.getName());
            dishDto.setCuisines(recipe.getCuisines().stream()
                    .map(Cuisine::getName)
                    .collect(Collectors.toList()));
            dishDto.setDescription(recipe.getSummary());

            NutritionalInfoDto nutritionalInfoDto = extractNutritionalInfo(recipe);
            dishDto.setNutrition(nutritionalInfoDto);

            return dishDto;
        }).collect(Collectors.toList());
    }

    private NutritionalInfoDto extractNutritionalInfo(Recipe recipe) {
        NutritionalInfoDto nutritionalInfoDto = new NutritionalInfoDto();

        Map<String, Double> nutrientsMap = recipe.getRecipeNutrients().stream()
                .collect(Collectors.toMap(
                        rn -> rn.getNutrient().getName(),
                        RecipeNutrient::getAmount
                ));
//TODO: use constants
        nutritionalInfoDto.setCalories(getNutrientAmount(nutrientsMap, "Calories"));
        nutritionalInfoDto.setCarbs(getNutrientAmount(nutrientsMap, "Carbohydrates"));
        nutritionalInfoDto.setProtein(getNutrientAmount(nutrientsMap, "Protein"));
        nutritionalInfoDto.setFat(getNutrientAmount(nutrientsMap, "Fat"));

        return nutritionalInfoDto;
    }

    private Double getNutrientAmount(Map<String, Double> nutrientsMap, String nutrientName) {
        return Optional.ofNullable(nutrientsMap.get(nutrientName))
                .orElseThrow(() -> new IllegalArgumentException(nutrientName + " nutrient not found"));
    }


    private List<Recipe> getSuggestedRecipes(Set<String> cuisine, int targetCalories) {
        return recipeRepository.getSuggestedRecipes(cuisine, targetCalories);
    }

    private int calculateCalories(String gender, String activityLevel) {
        if (gender.equals("man")) {
            return activityLevel.equals("active") ? 800 : 667;
        } else {
            return activityLevel.equals("active") ? 667 : 533;
        }
    }
}
