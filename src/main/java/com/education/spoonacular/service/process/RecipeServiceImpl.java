package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.RecipeNutrientDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final NutrientRepository nutrientRepository;
    private final CuisineService cuisineService;
    private final NutrientService nutrientService;

    public void processResponse(List<RecipeDto> recipeDtos) {
        List<NutrientDto> filteredNutrientsDtos = nutrientService.filter(recipeDtos);
        if (!filteredNutrientsDtos.isEmpty()) {
            nutrientService.saveAll(filteredNutrientsDtos);
        }

        Set<String> filteredCuisines = cuisineService.filter(recipeDtos);
        if (!filteredCuisines.isEmpty()) {
            cuisineService.saveAll(filteredCuisines);
        }

        List<RecipeDto> filteredRecipes = filter(recipeDtos);
        saveRecipe(filteredRecipes);
    }

    @Override
    public List<RecipeDto> filter(List<RecipeDto> recipeDtos) {
        ///TODO: url is unique not name

        Set<String> recipeUrls = recipeDtos.stream().map(RecipeDto::getUrl).collect(Collectors.toSet());
        Set<Recipe> existingInDBRecipes = findExistingInDB(recipeUrls);
        if (!existingInDBRecipes.isEmpty()) {
            List<String> existingRecipes = existingInDBRecipes.stream().map(Recipe::getUrl).toList();
            existingRecipes.forEach(recipeUrls::remove);
        }

        return recipeDtos.stream().filter(recipeDto -> recipeUrls.contains(recipeDto.getUrl())).collect(Collectors.toList());

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
            Set<Cuisine> cuisineSet = new HashSet<>();
            for (String cuisineDto : recipeDto.getCuisines()) {
                Cuisine cuisineRepositoryByName = cuisineService.findByName(cuisineDto)
                        .orElseThrow(() -> new IllegalStateException("Cuisine was not found " + cuisineDto));
                cuisineSet.add(cuisineRepositoryByName);
            }
            //TODO: Nutrient the same as Cuisine  -  .orElseThrow(()
            List<RecipeNutrient> recipeNutrients = new ArrayList<>();
            for (RecipeNutrientDto nutrient : recipeDto.getNutritionDto().getRecipeNutrientDtoList()) {
                RecipeNutrient recipeNutrient = new RecipeNutrient();
                Nutrient nutrientRepositoryByName = nutrientRepository.findByName(nutrient.getName())
                        .orElseThrow(() -> new IllegalStateException("Nutrient was not found " + nutrient.getName()));

                recipeNutrient.setAmount(nutrient.getAmount());
                recipeNutrient.setNutrient(nutrientRepositoryByName);
                recipeNutrients.add(recipeNutrient);
            }

            recipeEntity.setRecipeNutrients(recipeNutrients);
            recipeEntity.setCuisines(cuisineSet);

            recipeList.add(recipeEntity);
        }
        recipeRepository.saveAll(recipeList);
    }
}
