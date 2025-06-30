package com.education.spoonacular.service.mapper;

import com.education.spoonacular.dto.fetch.RecipeNutrientDto;
import com.education.spoonacular.dto.menu.ShoppingListDto;
import com.education.spoonacular.dto.menu.ShoppingListFlatDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    @Mapping(source = "ingredientName", target = "name")
    @Mapping(source = "ingredientUnit", target = "unit")
    @Mapping(source = "ingredientAmount", target = "amount")
    RecipeNutrientDto toNutrientDto(ShoppingListFlatDto flatDto);

    default List<ShoppingListDto> toShoppingList(List<ShoppingListFlatDto> flatDtos) {
        if (flatDtos == null || flatDtos.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, ShoppingListDto> grouped = new LinkedHashMap<>();

        for (ShoppingListFlatDto flat : flatDtos) {
            grouped.computeIfAbsent(flat.getRecipeId(), id ->
                    new ShoppingListDto(id, flat.getDish(), new ArrayList<>())
            );

            grouped.get(flat.getRecipeId())
                    .getIngredients()
                    .add(toNutrientDto(flat));
        }

        return new ArrayList<>(grouped.values());
    }
}
