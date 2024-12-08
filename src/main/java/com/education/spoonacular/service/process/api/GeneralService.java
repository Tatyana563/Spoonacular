package com.education.spoonacular.service.process.api;

import com.education.spoonacular.dto.RecipeDto;

import java.util.List;

public interface GeneralService<T> {
   void collectAndSaveNewEntities(List<RecipeDto> recipeDtos);

   List<T> findByNames(List<String> name);
}
