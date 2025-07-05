package com.education.spoonacular.service.mapper;

import com.education.spoonacular.db_view.RecipeDTO;
import com.education.spoonacular.db_view.RecipeDTOView;
import com.education.spoonacular.db_view.ViewIngredient;
import com.education.spoonacular.db_view.ViewNutrient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    RecipeDTO toDTO(RecipeDTOView view);

    List<RecipeDTO> toDTO(List<RecipeDTOView> views);

    @Named("jsonToStringSet")
    default Set<String> jsonToStringSet(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Set<String>>() {
            });
        } catch (Exception e) {
            return Set.of();
        }
    }

    @Named("jsonToNutrientList")
    default List<ViewNutrient> jsonToNutrientList(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<ViewNutrient>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }

    @Named("jsonToIngredientList")
    default List<ViewIngredient> jsonToIngredientList(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<ViewIngredient>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }
}

