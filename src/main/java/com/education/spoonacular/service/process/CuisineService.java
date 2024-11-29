package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;

import java.util.List;

public interface CuisineService extends GeneralService {

    List<Cuisine> saveAll(List<String> cuisine);

    List<Cuisine> findByNames(List<String> names);

    List<String> findExistingCuisineNamesInDB(List<String> cuisineList);

}
