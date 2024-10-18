package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>  {
    Recipe findByName(String name);
}
