package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.repository.NutrientRepository;
import com.education.spoonacular.repository.RecipeNutrientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeNutrientServiceImpl implements RecipeNutrientService {
private final RecipeNutrientRepository recipeNutrientRepository;


    @Override
    public void save(RecipeNutrient recipeNutrient) {
        recipeNutrientRepository.save(recipeNutrient);
    }
}
