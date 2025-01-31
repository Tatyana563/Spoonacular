package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.db_view.RecipeDTO;
import com.education.spoonacular.db_view.ViewIngredient;
import com.education.spoonacular.db_view.ViewNutrient;
import com.education.spoonacular.dto.fetch.RecipeNutrientDto;
import com.education.spoonacular.dto.menu.*;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.process.api.MenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper;

    public List<RecipeDTO> getSuggestedDishes(LunchRequestDto request) {

        int metabolicRate = calculateBasalMetabolicRate(request.getCharacteristicsDto());
        int energyExpenditure = (int) Math.round(calculateEnergyExpenditure(request.getCharacteristicsDto().getActivityLevel(), metabolicRate));
        Set<Long> cuisinePreferences = request.getCuisinePreferences();

        Set<String> allergens = request.getIngredientsExclusions();
        Long[] cuisineArray = (cuisinePreferences == null || cuisinePreferences.size() == 0) ? new Long[0] : cuisinePreferences.toArray(new Long[0]);
        String[] allergiesArray = (allergens == null || allergens.size() == 0) ? new String[0] : allergens.toArray(new String[0]);

        List<Tuple> suggestedRecipesForBreakfast = getSuggestedRecipesForBreakfast(cuisineArray, energyExpenditure, allergiesArray, MealType.BREAKFAST);
        return mapTuplesToRecipeDTO(suggestedRecipesForBreakfast);

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

    public List<RecipeDTO> mapTuplesToRecipeDTO(List<Tuple> tuples) {
        List<RecipeDTO> recipeDTOS = new ArrayList<>();

        for (Tuple tuple : tuples) {
            Integer recipeId = tuple.get("recipeId", Integer.class);
            String name = tuple.get("recipeName", String.class);
            String dishType = tuple.get("dishType", String.class);

            Integer[] cuisineArray = tuple.get("cuisines", Integer[].class);
            Set<Integer> cuisineName = cuisineArray != null ? new HashSet<>(Arrays.asList(cuisineArray)) : new HashSet<>();


            String nutrientJson = tuple.get("nutrient", String.class);
            List<ViewNutrient> nutrient = null;
            try {
                if (nutrientJson != null) {
                    nutrient = objectMapper.readValue(nutrientJson, new TypeReference<List<ViewNutrient>>() {
                    });
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String ingredientJson = tuple.get("ingredient", String.class);
            List<ViewIngredient> ingredient = null;
            try {
                if (ingredientJson != null) {
                    ingredient = objectMapper.readValue(ingredientJson, new TypeReference<List<ViewIngredient>>() {
                    });
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


            RecipeDTO recipeDTO = new RecipeDTO(recipeId, name, dishType, cuisineName, nutrient, ingredient);
            recipeDTOS.add(recipeDTO);
        }
        return recipeDTOS;
    }

    private List<Tuple> getSuggestedRecipesForBreakfast(Long[] cuisines, int targetCalories, String[] allergies, MealType mealType) {
        return recipeRepository.findBasicRecipes(cuisines, targetCalories, allergies, mealType.name());
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
