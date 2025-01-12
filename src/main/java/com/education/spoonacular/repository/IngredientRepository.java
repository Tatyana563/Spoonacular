package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    @Query("SELECT i FROM Ingredient i WHERE i.name IN :name")
    List<Ingredient> findByNames(List<String> name);

    @Query("SELECT i.name FROM Ingredient i WHERE i.name IN :ingredientDtoList")
    List<String> findExistingIngredientNamesInDB(Set<String> ingredientDtoList);
}
