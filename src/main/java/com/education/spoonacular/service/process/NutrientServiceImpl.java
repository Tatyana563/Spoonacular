package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.NutritionDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.repository.NutrientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutrientServiceImpl implements NutrientService {
    private final NutrientRepository nutrientRepository;

    @Transactional
    @Override
    public void collectAndSaveNewEntities(List<RecipeDto> recipeDtos) {
        Map<String, NutrientDto> nutrientDtoMap = collectUniqueNutrients(recipeDtos);

        //TODO: (ignore for now)  use batches for  nutrientDtoMap.values() findNonExistentByName(List<String> nutrientDtoList)
        List<String> existentByNameNutrients = nutrientRepository.findExistingNutrientNamesInDB(new ArrayList<>(nutrientDtoMap.keySet()));

        existentByNameNutrients.forEach(nutrientDtoMap::remove);

        nutrientDtoMap.values().stream().map(dto -> {
            //TODO: use mapstruct
            Nutrient nutrientEntity = new Nutrient();
            nutrientEntity.setName(dto.getName());
            nutrientEntity.setUnit(dto.getUnit());
            return nutrientEntity;
        }).forEach(nutrientRepository::save);

    }

    private Map<String, NutrientDto> collectUniqueNutrients(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .map(RecipeDto::getNutritionDto)
                .filter(Objects::nonNull)
                .map(NutritionDto::getRecipeNutrientDtoList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(recipeNutrientDto -> recipeNutrientDto.getName() != null && !recipeNutrientDto.getName().isEmpty())
                .map(recipeNutrientDto -> new NutrientDto(recipeNutrientDto.getName(), recipeNutrientDto.getUnit()))
                .collect(Collectors.toMap(
                        NutrientDto::getName,
                        Function.identity(),
                        (existing, replacement) -> existing));
    }

    @Override
    public List<Nutrient> findByNames(List<String> name) {
        return nutrientRepository.findByNames(name);
    }

}
