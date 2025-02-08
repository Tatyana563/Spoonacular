package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Filter;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "nutrient")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Nutrient extends ComponentBaseEntity {

}
