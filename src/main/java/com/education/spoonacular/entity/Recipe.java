package com.education.spoonacular.entity;

import com.education.spoonacular.view.RecipeDTOView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "recipe")
@NamedNativeQuery(
        name = "Recipe.findBasicRecipes",
        query = """
                    SELECT 
                        r.id AS recipeId,
                        r.name AS recipeName,
                        r.dish_type AS dishType,
                        jsonb_agg(DISTINCT c.name)::text AS cuisinesJson,
                        jsonb_agg(DISTINCT jsonb_build_object('name', n.name, 'amount', rn.amount, 'unit', n.unit))::text AS nutrientJson,
                        jsonb_agg(DISTINCT jsonb_build_object('name', i.name, 'amount', ri.amount, 'unit', i.unit))::text AS ingredientJson
                    FROM recipe r
                    JOIN recipe_nutrient rn ON r.id = rn.recipeid
                    JOIN nutrient n ON rn.nutrientid = n.id
                    JOIN recipe_ingredient ri ON r.id = ri.recipeid
                    JOIN ingredient i ON ri.ingredientid = i.id
                    JOIN recipe_cuisine rc ON r.id = rc.recipeid
                    JOIN cuisine c ON c.id = rc.cuisineid
                    WHERE n.name IN ('Carbohydrates', 'Protein', 'Fat', 'Calories')
                      AND r.id IN (:recipeIds)
                    GROUP BY r.id, r.name, r.dish_type
                """,
        resultSetMapping = "RecipeDTOViewMapping"
)
@SqlResultSetMapping(
        name = "RecipeDTOViewMapping",
        classes = @ConstructorResult(
                targetClass = RecipeDTOView.class,
                columns = {
                        @ColumnResult(name = "recipeId", type = Integer.class),
                        @ColumnResult(name = "recipeName", type = String.class),
                        @ColumnResult(name = "dishType", type = String.class),
                        @ColumnResult(name = "cuisinesJson", type = String.class),
                        @ColumnResult(name = "nutrientJson", type = String.class),
                        @ColumnResult(name = "ingredientJson", type = String.class)
                }
        )
)
@Data
@NoArgsConstructor
public class Recipe extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "dish_type", nullable = false)
    private String dishType;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "summary", nullable = false)
    private String summary;
    @Column(name = "vegetarian", nullable = false)
    private boolean vegetarian;
    @Column(name = "readyinminutes", nullable = false)
    private int readyInMinutes;

    @ElementCollection
    @CollectionTable(
            name = "recipe_nutrient",
            joinColumns = @JoinColumn(name = "recipeid")
    )
    private List<RecipeNutrient> recipeNutrients;

    @ElementCollection
    @CollectionTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipeid")
    )
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_cuisine",
            joinColumns = @JoinColumn(name = "recipeid"),
            inverseJoinColumns = @JoinColumn(name = "cuisineid")
    )
    public List<Cuisine> cuisines;
}
