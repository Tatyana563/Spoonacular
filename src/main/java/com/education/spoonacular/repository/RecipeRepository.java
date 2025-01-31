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

    @Query(value = """
                SELECT r.recipe_id AS recipeId,
                       r.recipe_name AS recipeName,
                       r.dish_type AS dishType,
                       array_agg(DISTINCT rc.cuisineid) AS cuisines, 
                       r.nutrient AS nutrient,
                       r.ingredient AS ingredient
                FROM recipe_nutrient_view r
                     JOIN recipe_cuisine rc ON r.recipe_id = rc.recipeid  
                WHERE r.recipe_id IN (:recipeIds)
                  GROUP BY r.recipe_id, r.recipe_name,r.dish_type, r.nutrient,r.ingredient
               
""", nativeQuery = true)
    List<Tuple> findBasicRecipes(@Param("recipeIds") List<Integer> recipeIds);

    @Query(value = """
               SELECT r.recipe_id AS recipeId
                  FROM recipe_nutrient_view r
                   JOIN recipe_cuisine rc ON r.recipe_id = rc.recipeid  
                   WHERE EXISTS (
                       SELECT 1 
                       FROM jsonb_array_elements(r.nutrient) AS elem
                       WHERE elem->>'name' = 'Calories'
                         AND CAST(elem->>'amount' AS DOUBLE PRECISION) < :targetCalories
                   )
              AND (:cuisinePreferences = '{}' OR rc.cuisineid = ANY(:cuisinePreferences))
                   AND r.dish_type = :mealType
              AND NOT EXISTS (
                     SELECT 1
                     FROM jsonb_array_elements(r.ingredient) AS ingr
                     WHERE ingr->>'name' ILIKE ANY (
                         SELECT '%' || unnest(:allergies) || '%'
                     )
                 )
                   GROUP BY r.recipe_id
               
            """, nativeQuery = true)
    List<Integer> findBasicRecipesIds(@Param("cuisinePreferences") Long[] cuisinePreferences, @Param("targetCalories") double targetCalories, @Param("allergies") String[] allergies, @Param("mealType") String mealType);


    //https://thorben-janssen.com/spring-data-jpa-query-projections/
    // Scalar Projections
    @Query("SELECT r.id AS id, r.name AS dish, " + "ri.ingredient.name AS ingredientName, ri.ingredient.unit AS ingredientUnit, ri.amount AS ingredientAmount " + "FROM Recipe r " + "JOIN r.recipeIngredients ri " + "WHERE r.id IN :recipeIds")
    List<Tuple> getShoppingList(@Param("recipeIds") Set<Integer> recipeIds);

}
