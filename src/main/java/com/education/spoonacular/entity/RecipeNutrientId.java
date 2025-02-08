package com.education.spoonacular.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeNutrientId implements Serializable {
    @Column(name = "recipeid")
    private Integer recipeIdCompound;

    @Column(name = "nutrientid")
    private Integer nutrientIdCompound;
}