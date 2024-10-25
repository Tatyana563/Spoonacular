package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.RecipeNutrientDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.CuisineRepository;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final CuisineRepository cuisineRepository;

    @Override
    public void save(RecipeDto recipe) {
        Recipe recipeEntity = new Recipe();
        recipeEntity.setName(recipe.getName());
        recipeEntity.setSummary(recipe.getSummary());
        recipeEntity.setVegetarian(recipe.isVegetarian());
        recipeEntity.setReadyInMinutes(recipe.getPreparationTime());

        List<RecipeNutrientDto> recipeNutrientDtos = recipe.getRecipeNutrientDtos();
        List<com.education.spoonacular.dto.Cuisine> cuisineDtos = recipe.getCuisines();
        Set<Cuisine> recipeCuisines = new HashSet<>();
        for (com.education.spoonacular.dto.Cuisine cuisineDto : cuisineDtos) {
            Cuisine cuisine = new Cuisine();
            cuisine.setName(cuisineDto.getName());
            recipeCuisines.add(cuisine);
        }

        recipeCuisines.stream()
                .filter(cuisine -> cuisineRepository.findByName(cuisine.getName()).isEmpty())  // Only keep non-existing cuisines
                .forEach(cuisineRepository::save);

        Set<Cuisine> cuisineSet = new HashSet<>();
        for (com.education.spoonacular.dto.Cuisine cuisineDto : cuisineDtos) {
            Optional<Cuisine> cuisineRepositoryByName = cuisineRepository.findByName(cuisineDto.getName());
            cuisineRepositoryByName.ifPresent(cuisineSet::add);
        }
        List<RecipeNutrient> recipeNutrients = new ArrayList<>();
        for (RecipeNutrientDto recipeNutrientDto : recipeNutrientDtos) {
            RecipeNutrient recipeNutrient = new RecipeNutrient();
            recipeNutrient.setNutrient(recipeNutrientDto.getNutrient());
            recipeNutrient.setAmount(recipeNutrientDto.getAmount());
            recipeNutrients.add(recipeNutrient);
        }

        recipeEntity.setRecipeNutrients(recipeNutrients);
        recipeEntity.setCuisines(cuisineSet);
        recipeRepository.save(recipeEntity);
    }

    @Override
    public Recipe findByName(String name) {
        return recipeRepository.findByName(name);
    }
}
