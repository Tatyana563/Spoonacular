package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.menu.LunchRequestDto;
import com.education.spoonacular.dto.menu.ShoppingListDto;

import java.util.List;
import java.util.Set;

public interface MenuService {

    List<DishDto> getSuggestedDishes(LunchRequestDto request);

    List<ShoppingListDto> getShoppingList(Set<Integer> dishIds);
}
