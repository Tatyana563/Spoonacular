package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
    //TODO: how will behave with duplicates
    @Query("SELECT n FROM Nutrient n WHERE n.name IN :name ")
    List<Nutrient> findByNames(List<String> name);

    @Query("SELECT n.name FROM Nutrient n WHERE n.name IN :nutrientDtoList ")
    List<String> findExistingNutrientNamesInDB(Set<String> nutrientDtoList);
}
