package com.education.spoonacular.service.mapper;

import com.education.spoonacular.view.RecipeDTO;
import com.education.spoonacular.view.RecipeDTOView;
import com.education.spoonacular.view.ViewIngredient;
import com.education.spoonacular.view.ViewNutrient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RecipeDTOViewToRecipeDTOMapper {

    @Mapping(source = "cuisinesJson", target = "cuisineName", qualifiedByName = "jsonToStringSet")
    @Mapping(source = "nutrientJson", target = "nutrient", qualifiedByName = "jsonToNutrientList")
    @Mapping(source = "ingredientJson", target = "ingredient", qualifiedByName = "jsonToIngredientList")
    RecipeDTO toDTO(RecipeDTOView view, @Context ObjectMapper objectMapper);

    List<RecipeDTO> toDTO(List<RecipeDTOView> views, @Context ObjectMapper objectMapper);

    @Named("jsonToStringSet")
    default Set<String> jsonToStringSet(String json, @Context ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, new TypeReference<Set<String>>() {
            });
        } catch (Exception e) {
            return Set.of();
        }
    }

    @Named("jsonToNutrientList")
    default List<ViewNutrient> jsonToNutrientList(String json, @Context ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<ViewNutrient>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }

    @Named("jsonToIngredientList")
    default List<ViewIngredient> jsonToIngredientList(String json, @Context ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<ViewIngredient>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }
}

