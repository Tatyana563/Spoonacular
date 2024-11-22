package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {
    List<RecipeDto> filter(List<RecipeDto> recipeDtos);

    Set<Recipe> findExistingInDB(Set<String> recipeNames);

    void saveRecipe(List<RecipeDto> recipeDtos);

}
