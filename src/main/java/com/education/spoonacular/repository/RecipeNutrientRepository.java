package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.RecipeNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeNutrientRepository extends JpaRepository<RecipeNutrient, Integer>  {

}
