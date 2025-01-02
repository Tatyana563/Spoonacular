package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;

import java.util.List;

public interface MenuService {

    List<DishDto> getSuggestedDishes(LunchRequestDto request);

}
