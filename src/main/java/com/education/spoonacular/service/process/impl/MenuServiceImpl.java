package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.config.ActivityLevelConfig;
import com.education.spoonacular.config.MacronutrientConfig;
import com.education.spoonacular.config.MetabolicRateConfig;
import com.education.spoonacular.config.SuggestedRecipesConfig;
import com.education.spoonacular.dto.menu.*;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.mapper.RecipeDTOViewToRecipeDTOMapper;
import com.education.spoonacular.service.mapper.ShoppingListMapper;
import com.education.spoonacular.service.process.api.MenuService;
import com.education.spoonacular.view.RecipeDTO;
import com.education.spoonacular.view.RecipeDTOView;
import com.education.spoonacular.view.ViewIngredient;
import com.education.spoonacular.view.ViewNutrient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper;
    private final ShoppingListMapper shoppingListMapper;
    private final RecipeDTOViewToRecipeDTOMapper recipeDTOMapper;
    private final SuggestedRecipesConfig suggestedRecipesConfig;
    private final ActivityLevelConfig activityLevelConfig;
    private final MacronutrientConfig macronutrientConfig;
    private final MetabolicRateConfig metabolicRateConfig;

    public List<RecipeDTO> getSuggestedDishes(LunchRequestDto request) {

        int metabolicRate = calculateBasalMetabolicRate(request.getCharacteristicsDto());
        int energyExpenditure = (int) Math.round(calculateEnergyExpenditure(request.getCharacteristicsDto().getActivityLevel(), metabolicRate));
        List<Integer> cuisinePreferences = request.getCuisinePreferences();

        Set<String> allergens = request.getIngredientsExclusions();

        Integer[] cuisineArray = (cuisinePreferences == null || cuisinePreferences.size() == 0) ? new Integer[0] : cuisinePreferences.toArray(new Integer[0]);
        String[] allergiesArray = (allergens == null || allergens.size() == 0) ? new String[0] : allergens.toArray(new String[0]);

        List<Integer> suggestedRecipesForLunch = getSuggestedRecipesIdsForBreakfast(cuisineArray, energyExpenditure, allergiesArray, MealType.LUNCH);
        List<Integer> randomIdsFromList = findRandomIdsFromList(suggestedRecipesForLunch);
        List<RecipeDTOView> suggestedRecipesForBreakfast = getSuggestedRecipesForBreakfast(randomIdsFromList);
        List<RecipeDTO> recipeDTOS = recipeDTOMapper.toDTO(suggestedRecipesForBreakfast);
        return recipeDTOS;
    }

    @Override
    public List<ShoppingListDto> getShoppingList(Set<Integer> dishIds) {
        List<ShoppingListFlatDto> shoppingList = recipeRepository.getShoppingList(dishIds);
        return shoppingListMapper.toShoppingList(shoppingList);
    }

    private List<Integer> findRandomIdsFromList(List<Integer> recipeIds) {

        Collections.shuffle(recipeIds);

        return recipeIds.stream()
                .limit(suggestedRecipesConfig.getAmount())
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> mapTuplesToRecipeDTO(List<Tuple> tuples) {
        List<RecipeDTO> recipeDTOS = new ArrayList<>();

        for (Tuple tuple : tuples) {
            Integer recipeId = tuple.get("recipeId", Integer.class);
            String name = tuple.get("recipeName", String.class);
            String dishType = tuple.get("dishType", String.class);

            String cuisineArray = tuple.get("cuisines", String.class);
            Set<String> cuisineName = cuisineArray != null ? new HashSet<>(Arrays.asList(cuisineArray)) : new HashSet<>();


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

    private List<Integer> getSuggestedRecipesIdsForBreakfast(Integer[] cuisines, int targetCalories, String[] allergies, MealType mealType) {
        return recipeRepository.findBasicRecipesIds(cuisines, targetCalories, allergies, mealType.name());
    }

    private List<RecipeDTOView> getSuggestedRecipesForBreakfast(List<Integer> recipeIds) {
        return recipeRepository.findBasicRecipes(recipeIds);
    }

    private int calculateBasalMetabolicRate(IndividualCharacteristicsDto characteristics) {
        int genderConstant = Gender.MAN.equals(characteristics.getGender())
                ? metabolicRateConfig.getMaleConstant()
                : metabolicRateConfig.getFemaleConstant();
        return (int) ((metabolicRateConfig.getWeightMultiplier() * characteristics.getWeight()) + (metabolicRateConfig.getHeightMultiplier() * characteristics.getHeight())
                - (metabolicRateConfig.getAgeMultiplier() * characteristics.getAge()) + genderConstant);
    }

    private Double calculateEnergyExpenditure(ActivityLevel activityLevel, int metabolisticRate) {
        return switch (activityLevel) {
            case SEDENTARY -> metabolisticRate * activityLevelConfig.getSedentary();
            case LIGHTLY_ACTIVE -> metabolisticRate * activityLevelConfig.getLightlyActive();
            case MODERATELY_ACTIVE -> metabolisticRate * activityLevelConfig.getModeratelyActive();
            case VERY_ACTIVE -> metabolisticRate * activityLevelConfig.getVeryActive();
            case SUPER_ACTIVE -> metabolisticRate * activityLevelConfig.getSuperActive();
            default -> throw new IllegalArgumentException("Unknown activity level: " + activityLevel);
        };
    }

    private MacronutrientDto calculateMacronutrient(IndividualCharacteristicsDto characteristics, int metabolisticRate) {
        double protein;
        double fat;
        double carbohydrates;
        if (characteristics.getActivityLevel().equals(ActivityLevel.SEDENTARY) || characteristics.getActivityLevel().equals(ActivityLevel.LIGHTLY_ACTIVE)) {
            protein = characteristics.getWeight() * macronutrientConfig.getProteinLowActivity();
        } else protein = characteristics.getWeight() * macronutrientConfig.getProteinHighActivity();

        fat = characteristics.getWeight();
        carbohydrates = metabolisticRate * macronutrientConfig.getCarbohydratesMultiplier();
        return new MacronutrientDto(protein, fat, carbohydrates);
    }
}
