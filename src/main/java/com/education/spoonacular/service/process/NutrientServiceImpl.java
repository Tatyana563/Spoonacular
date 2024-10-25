package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutrientServiceImpl implements NutrientService {
    private final NutrientRepository nutrientRepository;

    @Override
    public void save(NutrientDto nutrientDto) {
        Nutrient nutrientEntity = new Nutrient();
        nutrientEntity.setName(nutrientDto.getName());
        nutrientEntity.setUnit(nutrientDto.getUnit());
        nutrientRepository.save(nutrientEntity);

    }
    @Override
    public Nutrient findByName(String name) {
        return nutrientRepository.findByName(name);
    }
}
