package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.RecipeNutrientDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.process.api.CuisineService;
import com.education.spoonacular.service.process.api.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl extends AbstractGeneralService<Recipe, RecipeDto> implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final NutrientRepository nutrientRepository;
    private final CuisineService cuisineService;


    @Override
    protected String getUniqueIdentifier(RecipeDto dto) {
        return dto.getUrl();
    }

    @Override
    protected boolean isValidDto(RecipeDto dto) {
        return dto.getUrl() != null && !dto.getUrl().isEmpty();
    }

    @Override
    protected List<RecipeDto> extractDtos(RecipeDto recipeDto) {
        return List.of(recipeDto);
    }

    @Override
    protected Recipe createEntity(RecipeDto dto) {
        Recipe recipeEntity = new Recipe();
        recipeEntity.setName(dto.getName());
        recipeEntity.setSummary(dto.getSummary());
        recipeEntity.setVegetarian(dto.isVegetarian());
        recipeEntity.setReadyInMinutes(dto.getPreparationTime());
        recipeEntity.setUrl(dto.getUrl());

        List<Cuisine> savedCuisines = cuisineService.findByNames(dto.getCuisines());
        if (savedCuisines.size() != dto.getCuisines().size()) {
            throw new IllegalStateException(String.format("Cuisines were not found for recipe with name '%s' and url: '%s'",
                    dto.getName(), dto.getUrl()));
        }
        recipeEntity.setCuisines(savedCuisines);

        List<RecipeNutrient> recipeNutrients = new ArrayList<>();

        List<String> nutrientDtoNames = dto.getNutritionDto().getRecipeNutrientDtoList().stream().distinct().map(RecipeNutrientDto::getName).collect(Collectors.toList());

        List<Nutrient> nutrientRepositoryByName = nutrientRepository.findByNames(nutrientDtoNames);

        if (nutrientRepositoryByName.size() != nutrientDtoNames.size()) {
            throw new IllegalStateException(String.format("Nutrients amount from DB is not equivalent the required for recipe with name '%s' and url: '%s'",
                    dto.getName(), dto.getUrl()));
        }
        Map<String, RecipeNutrientDto> recipeNutrientDtoMap = dto.getNutritionDto().getRecipeNutrientDtoList().stream().collect(Collectors.toMap(
                RecipeNutrientDto::getName,
                recipeNutrientDto -> recipeNutrientDto
        ));


        for (Nutrient nutrient : nutrientRepositoryByName) {

            RecipeNutrient recipeNutrient = new RecipeNutrient();
            recipeNutrient.setNutrient(nutrient);

            recipeNutrient.setAmount(recipeNutrientDtoMap.get(nutrient.getName()).getAmount());
            recipeNutrients.add(recipeNutrient);
        }

        recipeEntity.setRecipeNutrients(recipeNutrients);
        return recipeEntity;
    }
//TODO:   protected List<String> findExistingEntityNames(--Set---<String> entityNames){
    @Override
    protected List<String> findExistingEntityNames(List<String> entityNames) {
        Set<String> entityNamesSet = new HashSet<>(entityNames);
        return recipeRepository.findExistingRecipeNames(entityNamesSet);
    }

    @Override
    protected void saveEntities(List<Recipe> entities) {
        recipeRepository.saveAll(entities);
    }

    @Override
    public List<Recipe> findByNames(List<String> name) {
        Set<String> reciepeUrls = new HashSet<>(name);
        return recipeRepository.findExistingInDB(reciepeUrls).stream().toList();
    }


    public List<DishDto> getSuggestedDishes(LunchRequestDto request) {

        int targetCalories = calculateCalories(request.getGender(), request.getActivityLevel());

        Set<String> cuisine = request.getCuisinePreferences();
        Set<Integer> allergens = request.getIngredientsExclusions();
        List<Recipe> suggestedRecipes = getSuggestedRecipes(cuisine, targetCalories);

        return null;
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
