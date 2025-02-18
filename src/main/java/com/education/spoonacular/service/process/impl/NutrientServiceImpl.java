package com.education.spoonacular.service.process.impl;


import com.education.spoonacular.dto.fetch.NutrientDto;
import com.education.spoonacular.dto.fetch.NutritionDto;
import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.dto.fetch.RecipeNutrientDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.service.mapper.NutrientDtoToNutrientMapper;
import com.education.spoonacular.service.process.api.NutrientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutrientServiceImpl extends AbstractGeneralService<Nutrient, NutrientDto> implements NutrientService {
    private final NutrientRepository nutrientRepository;
    private final NutrientDtoToNutrientMapper nutrientDtoToNutrientMapper;

    @Override
    public List<Nutrient> findByNames(List<String> name) {
        return nutrientRepository.findByNames(name);
    }

    @Override
    protected String getUniqueIdentifier(NutrientDto dto) {
        return dto.getName();
    }

    @Override
    protected boolean isValidDto(NutrientDto dto) {
        return dto.getName() != null && !dto.getName().isBlank();
    }

    @Override
    protected List<NutrientDto> extractDtos(RecipeDto recipeDto) {
        return Optional.ofNullable(recipeDto)
                .map(RecipeDto::getNutritionDto)
                .map(NutritionDto::getRecipeNutrientDtoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapRecipeNutrientDtoToNutrientDto)
                .collect(Collectors.toList());

    }

    private NutrientDto mapRecipeNutrientDtoToNutrientDto(RecipeNutrientDto recipeNutrientDto) {
        return new NutrientDto(recipeNutrientDto.getName(), recipeNutrientDto.getUnit());
    }

    @Override
    protected Nutrient createEntity(NutrientDto dto) {
        return nutrientDtoToNutrientMapper.mapNutrientDtoToNutrient(dto);
    }

    @Override
    protected List<String> findExistingEntityNames(Set<String> entityNames) {
        return nutrientRepository.findExistingNutrientNamesInDB(entityNames);
    }

    @Override
    protected void saveEntities(List<Nutrient> entities) {
        nutrientRepository.saveAll(entities);
    }
}
