package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "nutrient")
@NoArgsConstructor
public class Nutrient extends BaseEntity {
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "UNIT", nullable = false)
    private String unit;
}
