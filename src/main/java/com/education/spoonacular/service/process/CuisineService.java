package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CuisineService {

    Set<String> filter(List<RecipeDto> recipeDtos);

    List<Cuisine> saveAll(Set<String> cuisine);

    Optional<Cuisine> findByName(String name);

    List<Cuisine> findExistingInDB(Set<String> cuisineList);

}
