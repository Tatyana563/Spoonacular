package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.NutritionDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.repository.NutrientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutrientServiceImpl implements NutrientService {
    private final NutrientRepository nutrientRepository;

    @Override
    public List<NutrientDto> filter(List<RecipeDto> recipeDtos) {
        Map<String, NutrientDto> nutrientDtoMap = recipeDtos.stream()
                .map(RecipeDto::getNutritionDto)
                .filter(Objects::nonNull)
                .map(NutritionDto::getRecipeNutrientDtoList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(recipeNutrientDto -> recipeNutrientDto.getName() != null)
                .map(recipeNutrientDto -> new NutrientDto(recipeNutrientDto.getName(), recipeNutrientDto.getUnit()))
                .collect(Collectors.toMap(
                        NutrientDto::getName,
                        Function.identity(),

                        (existing, replacement) -> {
            System.out.println("Key collision for: " + existing.getName());
            System.out.println("Existing: " + existing + ", Replacement: " + replacement);
            if (!existing.equals(replacement)) {
                throw new IllegalStateException("Unexpected inequality for identical keys!");
            }
            return existing;
        }));
        //TODO:  use batches for  nutrientDtoMap.values() findNonExistentByName(List<String> nutrientDtoList)
        // return list of names not existent in DB; Create NutrientEntity abd save to DB move to NutrientService logic
        List<String> nutrientNames = new ArrayList<>(nutrientDtoMap.keySet());
        List<Nutrient> existentByNameNutrients = findExistingInDB(nutrientNames);
        if (existentByNameNutrients != null) {
            List<String> existingNutrients = existentByNameNutrients.stream().map(Nutrient::getName).toList();
            nutrientNames.removeAll(existingNutrients);
        }
       return nutrientNames.stream().map(nutrientDtoMap::get).collect(Collectors.toList());
    }

    @Override
    public List<Nutrient> saveAll(List<NutrientDto> nutrientDto) {
        Set<Nutrient> nutrients = nutrientDto.stream().map(dto -> {
            Nutrient nutrientEntity = new Nutrient();
            nutrientEntity.setName(dto.getName());
            nutrientEntity.setUnit(dto.getUnit());
            return nutrientEntity;
        }).collect(Collectors.toSet());
        return nutrientRepository.saveAll(nutrients);
    }

    @Override
    public Optional<Nutrient> findByName(String name) {
        return nutrientRepository.findByName(name);
    }

    @Override
    public List<Nutrient> findExistingInDB(List<String> nutrientDtoList) {
        return nutrientRepository.findExistingInDB(nutrientDtoList);
    }
}
