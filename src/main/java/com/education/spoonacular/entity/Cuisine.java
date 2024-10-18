package com.education.spoonacular.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "cuisine")
@NoArgsConstructor
public class Cuisine extends BaseEntity {
    private String name;
}
