package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.*;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final NutrientRepository nutrientRepository;
    private final CuisineService cuisineService;

    @Override
    public void collectAndSaveNewEntities(List<RecipeDto> recipeDtos) {

        Set<String> recipeUrls = recipeDtos.stream().map(RecipeDto::getUrl).collect(Collectors.toSet());
        Set<Recipe> existingInDBRecipes = findExistingInDB(recipeUrls);
        if (!existingInDBRecipes.isEmpty()) {
            List<String> existingRecipesUrl = existingInDBRecipes.stream().map(Recipe::getUrl).toList();
            existingRecipesUrl.forEach(recipeUrls::remove);
        }

        List<RecipeDto> recipiesToBeSaved = recipeDtos.stream().filter(recipeDto -> recipeUrls.contains(recipeDto.getUrl())).toList();
        saveRecipe(recipiesToBeSaved);
    }

    @Override
    public Set<Recipe> findExistingInDB(Set<String> recipeNames) {
        return recipeRepository.findExistingInDB(recipeNames);
    }

    @Override
    public void saveRecipe(List<RecipeDto> recipeDtos) {
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeDto recipeDto : recipeDtos) {

            Recipe recipeEntity = new Recipe();
            recipeEntity.setName(recipeDto.getName());
            recipeEntity.setSummary(recipeDto.getSummary());
            recipeEntity.setVegetarian(recipeDto.isVegetarian());
            recipeEntity.setReadyInMinutes(recipeDto.getPreparationTime());
            recipeEntity.setUrl(recipeDto.getUrl());

            //TODO: instead of findByName use findByNames - then check the size of a list if required throw  new IllegalStateException("Cuisine was not found " + cuisine));
            List<Cuisine> savedCuisines = cuisineService.findByNames(recipeDto.getCuisines());
            if (savedCuisines.size()!=recipeDto.getCuisines().size()) {
                throw new IllegalStateException(String.format("Cuisines were not found for recipe with name '%s' and url: '%s'",
                        recipeDto.getName(), recipeDto.getUrl()));
            }
            recipeEntity.setCuisines(savedCuisines);
            //TODO: Nutrient the same as Cuisine  -  .orElseThrow(()
            List<RecipeNutrient> recipeNutrients = new ArrayList<>();

                List<String> nutrientDtoNames = recipeDto.getNutritionDto().getRecipeNutrientDtoList().stream().map(RecipeNutrientDto::getName).collect(Collectors.toList());

                List<Nutrient> nutrientRepositoryByName = nutrientRepository.findByNames(nutrientDtoNames);

                if (nutrientRepositoryByName.size()!=recipeDto.getNutritionDto().getRecipeNutrientDtoList().size()) {
                    throw new IllegalStateException(String.format("Nutrients were not found for recipe with name '%s' and url: '%s'",
                            recipeDto.getName(), recipeDto.getUrl()));
                }
                List<RecipeNutrientDto> recipeNutrientDtoList = recipeDto.getNutritionDto().getRecipeNutrientDtoList();

                for (Nutrient nutrient : nutrientRepositoryByName) {

                    RecipeNutrient recipeNutrient = new RecipeNutrient();
                    recipeNutrient.setNutrient(nutrient);
                    recipeNutrient.setAmount(recipeNutrientDtoList.stream()
                            .filter(nutrientDto -> nutrientDto.getName().equals(nutrient.getName()))
                            .map(RecipeNutrientDto::getAmount)
                            .findFirst().get());
                    recipeNutrients.add(recipeNutrient);

            }

            recipeEntity.setRecipeNutrients(recipeNutrients);

            recipeList.add(recipeEntity);

            recipeRepository.saveAll(recipeList);
        }
    }

    @Override
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
