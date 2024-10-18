package com.education.spoonacular.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecipeNutrientId implements Serializable {
    @Column(name = "recipeid")
    private int recipeId;
    @Column(name = "nutrientid")
    private int nutrientId;
}
