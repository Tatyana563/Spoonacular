package com.education.spoonacular.service.process.impl;


import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.dto.fetch.RecipeNutrientFetchDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.entity.RecipeNutrientId;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeNutrientRepository;
import com.education.spoonacular.repository.RecipeRepository;
import com.education.spoonacular.service.process.api.RecipeNutrientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeNutrientServiceImpl extends AbstractGeneralService<RecipeNutrient, RecipeNutrientFetchDto> implements RecipeNutrientService {
    private final RecipeNutrientRepository recipeNutrientRepository;
    private final RecipeRepository recipeRepository;
    private final NutrientRepository nutrientRepository;

    @Override
    public List<RecipeNutrient> findByNames(List<String> name) {
        return recipeNutrientRepository.findByNames(name);
    }

    @Override
    protected String getUniqueIdentifier(RecipeNutrientFetchDto dto) {
        return dto.getRecipeUrl() + "|" + dto.getNutrientName();
    }

    @Override
    protected boolean isValidDto(RecipeNutrientFetchDto dto) {
        return !dto.getRecipeUrl().isBlank() && !dto.getNutrientName().isBlank();
    }

    @Override
    protected List<RecipeNutrientFetchDto> extractDtos(RecipeDto recipeDto) {
        List<RecipeNutrientFetchDto> recipeNutrientFetchDtos = new ArrayList<>();
        String url = recipeDto.getUrl();
        recipeDto.getNutritionDto().getRecipeNutrientDtoList().forEach(recipeNutrientDto -> {

            RecipeNutrientFetchDto dto = new RecipeNutrientFetchDto();
            dto.setNutrientName(recipeNutrientDto.getName());
            dto.setAmount(recipeNutrientDto.getAmount());
            dto.setRecipeUrl(url);
            recipeNutrientFetchDtos.add(dto);

        });

        return recipeNutrientFetchDtos;
    }

    @Override
    protected RecipeNutrient createEntity(RecipeNutrientFetchDto dto) {
        RecipeNutrient recipeNutrient = new RecipeNutrient();

        Recipe recipe = recipeRepository.findByUrl(dto.getRecipeUrl());
        recipeNutrient.setRecipe(recipe);
        Nutrient nutrient = nutrientRepository.findByName(dto.getNutrientName());
        recipeNutrient.setNutrient(nutrient);
        recipeNutrient.setAmount(dto.getAmount());
        recipeNutrient.setId(new RecipeNutrientId(recipe.getId(), nutrient.getId()));
        return recipeNutrient;
    }

    @Override
    protected List<String> findExistingEntityNames(Set<String> entityNames) {
        return recipeNutrientRepository.findExistingNutrientNamesInDB(entityNames);
    }

    @Override
    protected void saveEntities(List<RecipeNutrient> entities) {
        recipeNutrientRepository.saveAll(entities);
    }
}
