package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.RecipeNutrientDto;
import com.education.spoonacular.dto.menu.LunchRequestDto;
import com.education.spoonacular.dto.menu.ShoppingListDto;
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

        int targetCalories = calculateCalories(request.getGender(), request.getActivityLevel());

        Set<String> cuisine = request.getCuisinePreferences();
        Set<Integer> allergens = request.getIngredientsExclusions();
        List<Recipe> suggestedRecipes = getSuggestedRecipes(cuisine, targetCalories);
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
