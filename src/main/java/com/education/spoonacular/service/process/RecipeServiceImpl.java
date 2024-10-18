package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Override
    public void save(RecipeDto recipe) {
        Recipe recipeEntity = new Recipe();
        recipeEntity.setName(recipe.getName());
        recipeEntity.setSummary(recipe.getSummary());
        recipeEntity.setVegetarian(recipe.isVegetarian());
        recipeEntity.setReadyInMinutes(recipe.getPreparationTime());
        recipeRepository.save(recipeEntity);

    }

    @Override
    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findByName(String name) {
        return recipeRepository.findByName(name);
    }
}
