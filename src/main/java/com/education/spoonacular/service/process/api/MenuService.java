package com.education.spoonacular.service.process.api;

import com.education.spoonacular.view.RecipeDTO;
import com.education.spoonacular.dto.menu.LunchRequestDto;
import com.education.spoonacular.dto.menu.ShoppingListDto;

import java.util.List;
import java.util.Set;

public interface MenuService {

    List<RecipeDTO> getSuggestedDishes(LunchRequestDto request);

    List<ShoppingListDto> getShoppingList(Set<Integer> dishIds);
}
