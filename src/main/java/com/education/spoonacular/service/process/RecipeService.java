package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Recipe;

import java.util.List;

public interface RecipeService {
    void save(RecipeDto recipe);
    Recipe findByName(String name);
}
