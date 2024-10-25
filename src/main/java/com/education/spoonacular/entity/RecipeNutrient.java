package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Table(name = "recipe_nutrient")
@NoArgsConstructor
public class RecipeNutrient  {

    @ManyToOne
    @JoinColumn(name = "nutrientid")
    private Nutrient nutrient;

    @Column(name = "amount", nullable = false)
    private Double amount;

}
