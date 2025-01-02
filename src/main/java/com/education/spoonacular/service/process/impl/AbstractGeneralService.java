package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.service.process.api.GeneralService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//T - entity, D- dto
public abstract class AbstractGeneralService<T, D> implements GeneralService<T> {
    //TODO: add ingredients
    @Override
    @Transactional
    public void collectAndSaveNewEntities(List<RecipeDto> recipeDtos) {
        Map<String, D> namesDtoMap = collectUniqueDtos(recipeDtos);
        Set<String> entityNames = new HashSet<>(namesDtoMap.keySet());
        List<String> existingEntityNames = findExistingEntityNames(entityNames);
        if (existingEntityNames != null) {
            existingEntityNames.forEach(namesDtoMap::remove);
        }
        List<T> entities = namesDtoMap.values().stream().map(this::createEntity).collect(Collectors.toList());
        saveEntities(entities);
    }

    private Map<String, D> collectUniqueDtos(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .map(this::extractDtos)
                .flatMap(Collection::stream)
                .filter(this::isValidDto)
                .collect(Collectors.toMap(
                        this::getUniqueIdentifier,
                        Function.identity(),
                        (existing, replacement) -> existing));
    }

    protected abstract String getUniqueIdentifier(D dto);

    protected abstract boolean isValidDto(D dto);
    protected abstract List<D> extractDtos(RecipeDto recipeDto);

    protected abstract T createEntity(D dto);

    protected abstract List<String> findExistingEntityNames(Set<String> entityNames);

    protected abstract void saveEntities(List<T> entities);

}
