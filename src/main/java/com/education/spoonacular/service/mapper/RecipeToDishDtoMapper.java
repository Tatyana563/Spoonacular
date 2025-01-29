//package com.education.spoonacular.service.mapper;
//
//import com.education.spoonacular.dto.fetch.NutritionalInfoDto;
//import com.education.spoonacular.entity.Cuisine;
//import com.education.spoonacular.entity.Recipe;
//import com.education.spoonacular.entity.RecipeIngredient;
//import com.education.spoonacular.entity.RecipeNutrient;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Mapper(componentModel = "spring")
//public interface RecipeToDishDtoMapper {
//    String CALORIRS = "Calories";
//    String CARBOHYDRATES = "Carbohydrates";
//    String PROTEIN = "Protein";
//    String FAT = "Fat";
//
//    @Mapping(target = "description", source = "summary")
//    @Mapping(target = "ingredients", source = "recipeIngredients")
//    @Mapping(target = "nutrition", expression = "java(mapNutritionalInfo(recipe))")
//    DishDto mapRecipeToDishDto(Recipe recipe);
//
//    default List<String> mapCuisinesToNames(List<Cuisine> cuisines) {
//        if (cuisines == null) {
//            return Collections.emptyList();
//        }
//        return cuisines.stream().map(Cuisine::getName).collect(Collectors.toList());
//    }
//
//    default String mapIngredientsToNames(RecipeIngredient recipeIngredient) {
//        return recipeIngredient.getIngredient().getName();
//    }
//
//    default NutritionalInfoDto mapNutritionalInfo(Recipe recipe) {
//        NutritionalInfoDto nutritionalInfoDto = new NutritionalInfoDto();
//
//        Map<String, Double> nutrientsMap = recipe.getRecipeNutrients().stream().collect(Collectors.toMap(rn -> rn.getNutrient().getName(), RecipeNutrient::getAmount));
//
//        nutritionalInfoDto.setCalories(getNutrientAmount(nutrientsMap, CALORIRS));
//        nutritionalInfoDto.setCarbs(getNutrientAmount(nutrientsMap, CARBOHYDRATES));
//        nutritionalInfoDto.setProtein(getNutrientAmount(nutrientsMap, PROTEIN));
//        nutritionalInfoDto.setFat(getNutrientAmount(nutrientsMap, FAT));
//
//        return nutritionalInfoDto;
//    }
//
//    default Double getNutrientAmount(Map<String, Double> nutrientsMap, String nutrientName) {
//        return Optional.ofNullable(nutrientsMap.get(nutrientName)).orElseThrow(() -> new IllegalArgumentException(nutrientName + " nutrient not found"));
//    }
//
//}
