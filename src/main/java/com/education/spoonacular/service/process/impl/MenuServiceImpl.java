package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.fetch.DishDto;
import com.education.spoonacular.dto.fetch.RecipeNutrientDto;
import com.education.spoonacular.dto.menu.*;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.mapper.RecipeToDishDtoMapper;
import com.education.spoonacular.service.process.api.MenuService;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final RecipeRepository recipeRepository;
    private final RecipeToDishDtoMapper recipeToDishDtoMapper;

    public List<DishDto> getSuggestedDishes(LunchRequestDto request) {

        int metabolicRate = calculateBasalMetabolicRate(request.getCharacteristicsDto());
        int energyExpenditure = (int) Math.round(calculateEnergyExpenditure(request.getCharacteristicsDto().getActivityLevel(), metabolicRate));
        Set<String> cuisine = request.getCuisinePreferences();
        Set<String> allergens = request.getIngredientsExclusions();
        List<Recipe> suggestedRecipes = getSuggestedRecipesForBreakfast(cuisine, energyExpenditure, allergens);
        return convertRecipeToDishDtos(suggestedRecipes);
    }

    @Override
    public List<ShoppingListDto> getShoppingList(Set<Integer> dishIds) {
        return mapTuplesToShoppingList(recipeRepository.getShoppingList(dishIds));
    }

    public List<ShoppingListDto> mapTuplesToShoppingList(List<Tuple> tuples) {
        Map<Integer, ShoppingListDto> groupedByRecipe = new HashMap<>();

        for (Tuple tuple : tuples) {
            int recipeId = tuple.get("id", Integer.class);
            String dish = tuple.get("dish", String.class);
            String ingredientName = tuple.get("ingredientName", String.class);
            String ingredientUnit = tuple.get("ingredientUnit", String.class);
            Double ingredientAmount = tuple.get("ingredientAmount", Double.class);

            if (!groupedByRecipe.containsKey(recipeId)) {
                ShoppingListDto shoppingListDto = new ShoppingListDto(recipeId, dish, new ArrayList<>());
                groupedByRecipe.put(recipeId, shoppingListDto);
            }

            ShoppingListDto shoppingListDto = groupedByRecipe.get(recipeId);
            shoppingListDto.getIngredients().add(new RecipeNutrientDto(ingredientName, ingredientUnit, ingredientAmount));
        }

        return new ArrayList<>(groupedByRecipe.values());
    }

    //TODO: mapstruct
    public List<DishDto> convertRecipeToDishDtos(List<Recipe> suggestedRecipes) {
        return suggestedRecipes.stream().map(recipe -> {
            DishDto dishDto = recipeToDishDtoMapper.mapRecipeToDishDto(recipe);
            return dishDto;
        }).collect(Collectors.toList());
    }


    private List<Recipe> getSuggestedRecipesForBreakfast(Set<String> cuisine, int targetCalories, Set<String> allergies) {
        return recipeRepository.getSuggestedRecipes(cuisine, targetCalories, allergies);
    }

    private int calculateBasalMetabolicRate(IndividualCharacteristicsDto characteristics) {
        if (characteristics.getGender().equals(Gender.MAN)) {
            return (int) ((10 * characteristics.getWeight()) + (6.25 * characteristics.getHeight()) - (5 * characteristics.getAge()) + 5);
        } else {
            return (int) ((10 * characteristics.getWeight()) + (6.25 * characteristics.getHeight()) - (5 * characteristics.getAge()) - 161);
        }
    }

    private Double calculateEnergyExpenditure(ActivityLevel activityLevel, int metabolisticRate) {
        switch (activityLevel) {
            case SEDENTARY:
                return metabolisticRate * 1.2;
            case LIGHTLY_ACTIVE:
                return metabolisticRate * 1.375;
            case MODERATELY_ACTIVE:
                return metabolisticRate * 1.55;
            case VERY_ACTIVE:
                return metabolisticRate * 1.725;
            case SUPER_ACTIVE:
                return metabolisticRate * 1.9;
            default:
                throw new IllegalArgumentException("Unknown activity level: " + activityLevel);
        }
    }

    private MacronutrientDto calculateMacronutrient(IndividualCharacteristicsDto characteristics, int metabolisticRate) {
        double protein;
        double fat;
        double carbohydrates;
        if (characteristics.getActivityLevel().equals(ActivityLevel.SEDENTARY) || characteristics.getActivityLevel().equals(ActivityLevel.LIGHTLY_ACTIVE)) {
            protein = characteristics.getWeight() * 1.2;
        } else protein = characteristics.getWeight() * 1.4;

        fat = characteristics.getWeight();
        carbohydrates = metabolisticRate * 0.65 * 0.25;
        return new MacronutrientDto(protein, fat, carbohydrates);
    }
}
