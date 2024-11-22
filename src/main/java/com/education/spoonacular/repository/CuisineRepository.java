package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Integer> {
    Optional<Cuisine> findByName(String name);

    @Query("SELECT c FROM Cuisine c WHERE c.name IN :cuisineList ")
    List<Cuisine> findExistingInDB(@Param("cuisineList") Set<String> cuisineList);

}
