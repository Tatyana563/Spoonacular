package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;

import java.util.List;

public interface RecipeNutrientService {
    void save(RecipeNutrient recipeNutrient);

}
