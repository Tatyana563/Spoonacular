package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService extends GeneralService {

    Set<Recipe> findExistingInDB(Set<String> recipeNames);

    void saveRecipe(List<RecipeDto> recipeDtos);

    List<DishDto> getSuggestedDishes(LunchRequestDto request);

}
