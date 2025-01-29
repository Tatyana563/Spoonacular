package com.education.spoonacular.db_view;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewNutrient {
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "unit")
    private String unit;
}