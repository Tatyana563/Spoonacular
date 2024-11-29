package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;

import java.util.List;

public interface GeneralService {
   void collectAndSaveNewEntities(List<RecipeDto> recipeDtos);
}
