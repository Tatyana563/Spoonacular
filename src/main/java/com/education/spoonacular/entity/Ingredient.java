package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "ingredient")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
// TODO: create ComponentBaseEntity for Ingredient, Nutrient to have name and unit
public class Ingredient extends ComponentBaseEntity  {

}
