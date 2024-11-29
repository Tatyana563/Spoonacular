package com.education.spoonacular.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "cuisine")
@NoArgsConstructor
public class Cuisine extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public Cuisine(String name) {
        this.name = name;
    }
}
