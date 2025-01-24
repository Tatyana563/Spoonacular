package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.dto.fetch.RecipeIngredientDto;
import com.education.spoonacular.dto.fetch.RecipeNutrientDto;
import com.education.spoonacular.entity.*;
import com.education.spoonacular.repository.IngredientRepository;
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
public class RecipeServiceImpl extends AbstractGeneralService<Recipe, RecipeDto> implements  RecipeService{
    private final RecipeRepository recipeRepository;
    private final NutrientRepository nutrientRepository;
    private final IngredientRepository ingredientRepository;
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
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        List<String> nutrientDtoNames = dto.getNutritionDto().getRecipeNutrientDtoList().stream().distinct().map(RecipeNutrientDto::getName).collect(Collectors.toList());
        List<String> ingredientDtoNames = dto.getNutritionDto().getRecipeIngredientDto().stream().distinct().map(RecipeIngredientDto::getName).toList();

        List<Nutrient> nutrientRepositoryByName = nutrientRepository.findByNames(nutrientDtoNames);
        List<Ingredient> ingredientRepositoryByName = ingredientRepository.findByNames(ingredientDtoNames);

        if (nutrientRepositoryByName.size() != nutrientDtoNames.size()) {
            throw new IllegalStateException(String.format("Nutrients amount from DB is not equivalent the required for recipe with name '%s' and url: '%s'",
                    dto.getName(), dto.getUrl()));
        }

        if (ingredientRepositoryByName.size() != ingredientDtoNames.stream().distinct().collect(Collectors.toList()).size()) {
            throw new IllegalStateException(String.format("Ingredients amount from DB is not equivalent the required for recipe with name '%s' and url: '%s'",
                    dto.getName(), dto.getUrl()));
        }
        Map<String, RecipeNutrientDto> recipeNutrientDtoMap = dto.getNutritionDto().getRecipeNutrientDtoList().stream().collect(Collectors.toMap(
                RecipeNutrientDto::getName,
                recipeNutrientDto -> recipeNutrientDto
        ));

        Map<String, RecipeIngredientDto> recipeIngredientDtoMap = dto.getNutritionDto().getRecipeIngredientDto().stream().collect(Collectors.toMap(
                RecipeIngredientDto::getName,
                recipeNutrientDto -> recipeNutrientDto,
                (existing, replacement) -> {
                    existing.setAmount(existing.getAmount() + replacement.getAmount());
                    return existing;
                }
        ));


        for (Nutrient nutrient : nutrientRepositoryByName) {

            RecipeNutrient recipeNutrient = new RecipeNutrient();
            recipeNutrient.setNutrient(nutrient);

            recipeNutrient.setAmount(recipeNutrientDtoMap.get(nutrient.getName()).getAmount());
            recipeNutrients.add(recipeNutrient);
        }

        for (Ingredient ingredient : ingredientRepositoryByName) {

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setIngredient(ingredient);

            recipeIngredient.setAmount(recipeIngredientDtoMap.get(ingredient.getName()).getAmount());
            recipeIngredients.add(recipeIngredient);
        }

        recipeEntity.setRecipeNutrients(recipeNutrients);
        recipeEntity.setRecipeIngredients(recipeIngredients);
        return recipeEntity;
    }

    @Override
    protected List<String> findExistingEntityNames(Set<String> entityNames) {
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

}
