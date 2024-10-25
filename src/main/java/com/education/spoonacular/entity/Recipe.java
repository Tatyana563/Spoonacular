package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "recipe")
@NoArgsConstructor
public class Recipe extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
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

    @OneToMany
    @JoinTable(
            name="recipe_cuisine",
            joinColumns = @JoinColumn( name="recipeid"),
            inverseJoinColumns = @JoinColumn( name="cuisineid")
    )
    public Set<Cuisine> cuisines;
}
