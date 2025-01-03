package com.education.spoonacular.service.mapper;

import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.entity.Nutrient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NutrientDtoToNutrientMapper {

     Nutrient mapNutrientDtoToNutrient(NutrientDto nutrientDto);
}
