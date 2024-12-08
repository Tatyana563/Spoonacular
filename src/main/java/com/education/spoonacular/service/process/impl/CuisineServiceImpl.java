package com.education.spoonacular.service.process.impl;


import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.repository.CuisineRepository;
import com.education.spoonacular.service.process.api.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl extends AbstractGeneralService<Cuisine, String> implements CuisineService {
    private final CuisineRepository cuisineRepository;

    @Override
    protected String getUniqueIdentifier(String dto) {
        return dto;
    }

    @Override
    protected boolean isValidDto(String dto) {
        return dto != null && !dto.isEmpty();
    }

    @Override
    protected List<String> extractDtos(RecipeDto recipeDto) {
        return Optional.ofNullable(recipeDto)
                .map(RecipeDto::getCuisines)
                .orElseGet(Collections::emptyList)
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    protected Cuisine createEntity(String dto) {
        Cuisine cuisine = new Cuisine();
        cuisine.setName(dto);
        return cuisine;
    }

    @Override
    protected List<String> findExistingEntityNames(Set<String> entityNames) {
        return cuisineRepository.findExistingCuisineNamesInDB(entityNames);
    }

    @Override
    protected void saveEntities(List<Cuisine> entities) {
        cuisineRepository.saveAll(entities);
    }

    @Override
    public List<Cuisine> findByNames(List<String> names) {
        return cuisineRepository.findByNames(names);
    }


}
