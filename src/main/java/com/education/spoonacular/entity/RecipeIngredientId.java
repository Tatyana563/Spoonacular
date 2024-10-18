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
public class RecipeIngredientId implements Serializable {
    @Column(name = "recipeid")
    private int recipeid;
    @Column(name = "ingredientid")
    private int ingredientid;
}
