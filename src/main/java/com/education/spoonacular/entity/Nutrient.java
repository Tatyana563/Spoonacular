package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "nutrient")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

public class Nutrient extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit", nullable = false)
    private String unit;
}
