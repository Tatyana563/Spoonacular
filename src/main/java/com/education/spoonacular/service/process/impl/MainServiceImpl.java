package com.education.spoonacular.service.process.impl;

import com.education.spoonacular.dto.fetch.NutritionDto;
import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.service.process.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class MainServiceImpl implements MainService {
    private final NutrientService nutrientService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private static final List<String> REQUIRED_NUTRIENTS = List.of("Calories", "Carbohydrates", "Protein", "Fat");

    public void processData(List<RecipeDto> recipeDtos) {
        Stream<RecipeDto> filteredRecipes = filterRecipes(recipeDtos);
        processResponse(filteredRecipes.collect(Collectors.toList()));
    }
    public void processResponse(List<RecipeDto> recipeDtos) {
        List<RecipeDto> filteredRecipeDtos = filterRecipiesWithFaultIngredients(recipeDtos);
        List.of(cuisineService, nutrientService, ingredientService,recipeService)
                .forEach(generalService -> generalService.collectAndSaveNewEntities(filteredRecipeDtos));
    }

    private List<RecipeDto> filterRecipiesWithFaultIngredients(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .filter(recipeDto -> recipeDto.getNutritionDto() != null &&
                        recipeDto.getNutritionDto().getRecipeIngredientDto() != null &&
                        recipeDto.getNutritionDto().getRecipeIngredientDto().stream()
                                .allMatch(dto -> dto.getUnit() != null && !dto.getUnit().isEmpty()&& dto.getAmount() != null))
                .collect(Collectors.toList());
    }

    private Stream<RecipeDto> filterRecipes(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .filter(filterRecipesWithEmptyUrls())
                .filter(filterDuplicates(RecipeDto::getUrl))
                .filter(this::hasCompleteNutrients);
    }

    private Predicate<RecipeDto> filterRecipesWithEmptyUrls() {
        return recipeDto -> !recipeDto.getUrl().isEmpty();
    }

    private <T> Predicate<T> filterDuplicates(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private boolean hasCompleteNutrients(RecipeDto recipeDto) {

        return Stream.of(recipeDto)
                .filter(Objects::nonNull)
                .map(RecipeDto::getNutritionDto)
                .filter(Objects::nonNull)
                .map(NutritionDto::getRecipeNutrientDtoList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(recipeNutrientDto -> REQUIRED_NUTRIENTS.contains(recipeNutrientDto.getName()))
                .filter(recipeNutrientDto -> recipeNutrientDto.getAmount() != null && recipeNutrientDto.getUnit() != null)
                .count() == REQUIRED_NUTRIENTS.size();

    }

}
