package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Integer>  {
    Nutrient findByName(String name);
}
