package com.education.spoonacular.service.process.impl;


import com.education.spoonacular.dto.IngredientDto;
import com.education.spoonacular.dto.NutritionDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.RecipeIngredientDto;
import com.education.spoonacular.entity.Ingredient;
import com.education.spoonacular.repository.IngredientRepository;
import com.education.spoonacular.service.process.api.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl extends AbstractGeneralService<Ingredient, IngredientDto> implements IngredientService {
    private final IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> findByNames(List<String> name) {
        return ingredientRepository.findByNames(name);
    }

    @Override
    protected String getUniqueIdentifier(IngredientDto dto) {
        return dto.getName();
    }

    @Override
    protected boolean isValidDto(IngredientDto dto) {
        return dto.getName() != null && !dto.getName().isBlank();
    }

    @Override
    protected List<IngredientDto> extractDtos(RecipeDto recipeDto) {
        return Optional.ofNullable(recipeDto)
                .map(RecipeDto::getNutritionDto)
                .map(NutritionDto::getRecipeIngredientDto)
                .map(this::mapRecipeIngredientDtoToIngredientDto)
                .orElseGet(Collections::emptyList);
    }

    private List<IngredientDto> mapRecipeIngredientDtoToIngredientDto(List<RecipeIngredientDto> dto) {
        return dto.stream()
                .map(dto1 -> new IngredientDto(dto1.getName(), dto1.getUnit()))
                .collect(Collectors.toList());
    }

    @Override
    protected Ingredient createEntity(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setUnit(dto.getUnit());
        return ingredient;
    }

    @Override
    protected List<String> findExistingEntityNames(Set<String> entityNames) {
        return ingredientRepository.findExistingIngredientNamesInDB(entityNames);
    }

    @Override
    protected void saveEntities(List<Ingredient> entities) {
        ingredientRepository.saveAll(entities);
    }
}
