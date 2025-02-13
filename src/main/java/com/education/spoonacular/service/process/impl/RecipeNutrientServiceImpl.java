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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeNutrientServiceImpl extends AbstractGeneralService<RecipeNutrient, RecipeNutrientFetchDto> implements RecipeNutrientService {
    //TODO: use ThreadLocal
    private final ThreadLocal<Map<String, Nutrient>> nutrientsMap = ThreadLocal.withInitial(HashMap::new);
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
        List<String> nutrientNames = new ArrayList<>();
       // nutrientsMap.get().clear();
        List<RecipeNutrientFetchDto> recipeNutrientFetchDtos = new ArrayList<>();
        String url = recipeDto.getUrl();
        recipeDto.getNutritionDto().getRecipeNutrientDtoList().forEach(recipeNutrientDto -> {
            nutrientNames.add(recipeNutrientDto.getName());
            RecipeNutrientFetchDto dto = new RecipeNutrientFetchDto();
            dto.setNutrientName(recipeNutrientDto.getName());
            dto.setAmount(recipeNutrientDto.getAmount());
            dto.setRecipeUrl(url);
            recipeNutrientFetchDtos.add(dto);

            // TODO:
            // collect all NutrientNames
            // get List<Nutrients> from db by NutrientNames
            // Map<NutrientName, Nutrient>
        });
        List<Nutrient> nutrientsByNames = nutrientRepository.findByNames(nutrientNames);
        nutrientsByNames.stream().forEach(nutrient -> {
            nutrientsMap.get().put(nutrient.getName(),nutrient);
        });
        return recipeNutrientFetchDtos;
    }

    @Override
    protected RecipeNutrient createEntity(RecipeNutrientFetchDto dto) {
        RecipeNutrient recipeNutrient = new RecipeNutrient();
        //TODO: look for by id not url
        Recipe recipe = recipeRepository.findByUrl(dto.getRecipeUrl());
        recipeNutrient.setRecipe(recipe);
        //TODO: use map nutrients instead of ( Nutrient nutrient = nutrientRepository.findByName(dto.getNutrientName());)
      //  Nutrient nutrient = nutrientRepository.findByName(dto.getNutrientName());
        Nutrient nutrient = nutrientsMap.get().get(dto.getNutrientName());
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
