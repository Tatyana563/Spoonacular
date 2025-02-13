package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "recipe")
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

    @OneToMany(mappedBy = "recipe",fetch = FetchType.LAZY)
    @ToString.Exclude
    public List<RecipeNutrient> recipeNutrients;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    @ToString.Exclude
    public List<BasicRecipeNutrient> basicRecipeNutrients;
}
