package com.education.spoonacular.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "recipe")
@NoArgsConstructor
public class Recipe extends BaseEntity {
    private String name;
    private String summary;
    private boolean vegetarian;
    @Column(name = "readyinminutes", nullable = false)
    private String readyInMinutes;
}
