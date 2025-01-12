package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Recipe;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.cuisines WHERE r.url IN :urls")
    List<Recipe> findExistingInDB(@Param("urls") Set<String> urls);

    @Query("SELECT r.url FROM Recipe r WHERE r.url IN :urls")
    List<String> findExistingRecipeNames(@Param("urls") Set<String> urls);


    @Query(" SELECT DISTINCT r FROM Recipe r " +
            " JOIN r.recipeNutrients rn  " +
            " JOIN rn.nutrient n JOIN r.cuisines c" +
            " WHERE n.name = 'Calories' AND rn.amount <= :targetCalories AND c.name IN :cuisines ")
    List<Recipe> getSuggestedRecipes(Set<String> cuisines, int targetCalories);

    //https://thorben-janssen.com/spring-data-jpa-query-projections/
    // Scalar Projections
    @Query("SELECT r.id AS id, r.name AS dish, " +
            "ri.ingredient.name AS ingredientName, ri.ingredient.unit AS ingredientUnit, ri.amount AS ingredientAmount " +
            "FROM Recipe r " +
            "JOIN r.recipeIngredients ri " +
            "WHERE r.id IN :recipeIds")
    List<Tuple> getShoppingList(@Param("recipeIds") Set<Integer> recipeIds);

}
