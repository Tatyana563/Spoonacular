package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;

import java.util.List;

public interface MainService {

     void processResponse(List<RecipeDto> recipeDtos);

}
