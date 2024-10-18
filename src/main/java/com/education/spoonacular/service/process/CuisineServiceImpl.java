package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.CuisineDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.repository.CuisineRepository;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {
    private final CuisineRepository cuisineRepository;

    @Override
    public void save(CuisineDto cuisineDto) {
        Cuisine cuisine = new Cuisine();
        cuisine.setName(cuisineDto.getName());
        cuisineRepository.save(cuisine);
    }

    @Override
    public Cuisine findByName(String name) {
        return cuisineRepository.findByName(name);
    }
}
