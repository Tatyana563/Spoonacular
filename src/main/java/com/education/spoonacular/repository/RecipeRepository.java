package com.education.spoonacular.repository;

import com.education.spoonacular.view.RecipeDTOView;
import com.education.spoonacular.dto.menu.ShoppingListFlatDto;
import com.education.spoonacular.entity.Recipe;
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

    @Query(name = "Recipe.findBasicRecipes", nativeQuery = true)
    List<RecipeDTOView> findBasicRecipes(@Param("recipeIds") List<Integer> recipeIds);

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
          SELECT 1 FROM recipe_cuisine rc
          WHERE rc.recipeid = r.id
            AND rc.cuisineid = ANY(:cuisinePreferences)
      ))
      AND r.dish_type = :mealType
      AND r.id NOT IN (
          SELECT ri2.recipeid
          FROM recipe_ingredient ri2
          JOIN ingredient i2 ON ri2.ingredientid = i2.id
          WHERE EXISTS (
              SELECT 1 FROM unnest(:allergies) AS allergy
              WHERE i2.name ILIKE '%' || allergy || '%'
          )
      )
    GROUP BY r.id
    """, nativeQuery = true)
    List<Integer> findBasicRecipesIds(
            @Param("cuisinePreferences") Integer[] cuisinePreferences,
            @Param("targetCalories") double targetCalories,
            @Param("allergies") String[] allergies,
            @Param("mealType") String mealType
    );



    //https://thorben-janssen.com/spring-data-jpa-query-projections/
    @Query("SELECT new com.education.spoonacular.dto.menu.ShoppingListFlatDto(r.id, r.name, ri.ingredient.name, ri.ingredient.unit, ri.amount) " +
            "FROM Recipe r " +
            "JOIN r.recipeIngredients ri " +
            "WHERE r.id IN :recipeIds")
    List<ShoppingListFlatDto> getShoppingList(@Param("recipeIds") Set<Integer> recipeIds);

}
