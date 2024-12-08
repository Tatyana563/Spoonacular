package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.entity.Recipe;

import java.util.List;

public interface RecipeService extends GeneralService<Recipe> {

    List<DishDto> getSuggestedDishes(LunchRequestDto request);

}
