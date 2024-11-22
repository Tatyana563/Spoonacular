package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
    Optional<Nutrient> findByName(String name);

    @Query("SELECT n FROM Nutrient n WHERE n.name IN :nutrientDtoList ")
    List<Nutrient> findExistingInDB(@Param("nutrientDtoList") List<String> nutrientDtoList);
}
