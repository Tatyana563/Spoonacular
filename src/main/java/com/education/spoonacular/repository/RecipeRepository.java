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
    Recipe findByUrl(String url);
    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.cuisines WHERE r.url IN :urls")
    List<Recipe> findExistingInDB(@Param("urls") Set<String> urls);

    @Query("SELECT r.url FROM Recipe r WHERE r.url IN :urls")
    List<String> findExistingRecipeNames(@Param("urls") Set<String> urls);

    @Query("""
    SELECT r.id AS recipeId, 
           r.name AS recipeName, 
           r.dishType AS dishType, 
           STRING_AGG(c.name, ', ') AS cuisines,
   i.name AS ingredientName,
   i.unit AS ingredientUnit,
   ri.amount AS ingredientAmount,
   
   n.name AS nutrientName,
   n.unit AS nutrientUnit,
   rn.amount AS nutrientAmount
   
    FROM Recipe r 
    LEFT JOIN r.cuisines c 
    LEFT JOIN r.recipeIngredients ri
    LEFT JOIN ri.ingredient i 
    LEFT JOIN r.recipeNutrients rn
    LEFT JOIN rn.nutrient n 
    WHERE r.id IN :recipeIds
    AND n.name IN ('Carbohydrates', 'Protein', 'Fat', 'Calories')
    GROUP BY r.id, r.name, r.dishType, i.name, i.unit, ri.amount,n.name,n.unit,rn.amount
""")
    List<Tuple> findBasicRecipes(@Param("recipeIds") List<Integer> recipeIds);

    @Query(value = """
    SELECT r.id AS recipeId
    FROM Recipe r
    JOIN recipe_nutrient rn ON r.id = rn.recipeid
    JOIN nutrient n ON rn.nutrientid = n.id
    JOIN recipe_ingredient ri ON r.id = ri.recipeid
    JOIN ingredient i ON ri.ingredientid = i.id
    WHERE n.name = 'Calories'
      AND rn.amount < :targetCalories
      AND (:cuisinePreferences = '{}' OR EXISTS (
          SELECT 1 FROM recipe_cuisine rc WHERE rc.recipeid = r.id AND rc.cuisineid = ANY(:cuisinePreferences)
      ))
      AND r.dish_type = :mealType
      AND NOT EXISTS (
          SELECT 1 FROM recipe_ingredient ri
          WHERE ri.recipeid = r.id
            AND EXISTS (
                SELECT 1 FROM unnest(:allergies) AS allergy
                WHERE i.name ILIKE '%' || allergy || '%'
            )
      )
    GROUP BY r.id
    """, nativeQuery = true)
    List<Integer> findBasicRecipesIds(
            @Param("cuisinePreferences") Long[] cuisinePreferences,
            @Param("targetCalories") double targetCalories,
            @Param("allergies") String[] allergies,
            @Param("mealType") String mealType
    );



    //https://thorben-janssen.com/spring-data-jpa-query-projections/
    // Scalar Projections
    @Query("SELECT r.id AS id, r.name AS dish, " + "ri.ingredient.name AS ingredientName, ri.ingredient.unit AS ingredientUnit, ri.amount AS ingredientAmount " + "FROM Recipe r " + "JOIN r.recipeIngredients ri " + "WHERE r.id IN :recipeIds")
    List<Tuple> getShoppingList(@Param("recipeIds") Set<Integer> recipeIds);

}
