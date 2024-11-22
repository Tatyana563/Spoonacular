package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;

import java.util.List;
import java.util.Optional;

public interface NutrientService {
    //TODO: (ignore for now): return map <NutrientName, Nutrient> of all existent and non existent but now saved in order
    // to use for  Recipe not to make extra call to DB

    List<NutrientDto>  filter(List<RecipeDto> recipeDtos);
    List<Nutrient> saveAll(List<NutrientDto> nutrientDto);

    Optional<Nutrient> findByName(String name);
    List<Nutrient> findExistingInDB( List<String> nutrientDtoList);

}
