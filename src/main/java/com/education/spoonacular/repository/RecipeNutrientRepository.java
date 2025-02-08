package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.RecipeNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeNutrientRepository extends JpaRepository<RecipeNutrient, Integer> {
    @Query("SELECT rn FROM RecipeNutrient rn JOIN Nutrient n WHERE n.name IN :name")
    List<RecipeNutrient> findByNames(List<String> name);

    @Query("SELECT rn.nutrient.name FROM RecipeNutrient rn  WHERE rn.nutrient.name IN :nutrientDtoList")
    List<String> findExistingNutrientNamesInDB(Set<String> nutrientDtoList);
}
