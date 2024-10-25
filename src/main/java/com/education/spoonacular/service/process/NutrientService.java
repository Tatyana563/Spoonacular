package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;

import java.util.List;

public interface NutrientService {
    void save(NutrientDto nutrientDto);

    Nutrient findByName(String name);
}
