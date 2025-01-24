package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.fetch.RecipeDto;

import java.util.List;

public interface MainService {

     void processResponse(List<RecipeDto> recipeDtos);

}
