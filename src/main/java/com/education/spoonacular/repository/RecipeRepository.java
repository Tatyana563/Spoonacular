package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Optional<Recipe> findByName(String name);

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.cuisines WHERE r.url IN :urls")
    Set<Recipe> findExistingInDB(@Param("urls") Set<String> urls);
}
