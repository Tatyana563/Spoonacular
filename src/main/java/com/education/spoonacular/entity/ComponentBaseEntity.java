package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class ComponentBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // unique = true in case scheme generated by Hibernate
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "unit", nullable = false)
    private String unit;
}