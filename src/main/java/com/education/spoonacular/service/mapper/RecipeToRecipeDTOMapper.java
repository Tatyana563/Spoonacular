package com.education.spoonacular.service.mapper;

import com.education.spoonacular.db_view.RecipeDTO;
import com.education.spoonacular.dto.fetch.IngredientAmountDto;
import com.education.spoonacular.dto.fetch.NutrientAmountDto;
import com.education.spoonacular.entity.BasicRecipeNutrient;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecipeToRecipeDTOMapper {
    @Mapping(target = "recipeId", source = "id")
    @Mapping(target = "recipeName", source = "name")
    @Mapping(target = "dishType", source = "dishType")
    @Mapping(target = "cuisineName", expression = "java(mapCuisines(recipe.getCuisines()))")
    @Mapping(target = "nutrients", expression = "java(mapNutrients(recipe.getBasicRecipeNutrients()))")
    @Mapping(target = "ingredients", expression = "java(mapIngredients(recipe.getRecipeIngredients()))")
    RecipeDTO mapRecipeToRecipeDTO(Recipe recipe);

    List<RecipeDTO> mapRecipeToRecipeDTO(List<Recipe> recipes);

    default Set<String> mapCuisines(List<Cuisine> cuisines) {
        return cuisines != null ?
                cuisines.stream().map(Cuisine::getName).collect(Collectors.toSet())
                : Collections.emptySet();
    }
    default Set<NutrientAmountDto> mapNutrients(List<BasicRecipeNutrient> basicRecipeNutrients){
        return basicRecipeNutrients.stream().map(dto ->
           new NutrientAmountDto(dto.getNutrient().getName(), dto.getAmount(), dto.getNutrient().getUnit())).collect(Collectors.toSet());
    }

    default Set<IngredientAmountDto> mapIngredients(List<RecipeIngredient> recipeIngredients){
       return recipeIngredients.stream().map(recipeIngredient ->
                new IngredientAmountDto(
                        recipeIngredient.getIngredient().getName(),recipeIngredient.getAmount(),recipeIngredient.getIngredient().getUnit()))
               .collect(Collectors.toSet());
    }
}
