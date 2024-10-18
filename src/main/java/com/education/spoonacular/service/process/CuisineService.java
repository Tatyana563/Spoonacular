package com.education.spoonacular.service.process;

import com.education.spoonacular.dto.CuisineDto;
import com.education.spoonacular.entity.Cuisine;

public interface CuisineService {
    void save(CuisineDto cuisineDto);

    Cuisine findByName(String name);
}
