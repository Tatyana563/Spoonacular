package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Integer> {
    @Query("SELECT c FROM Cuisine c WHERE c.name IN :names ")
    List<Cuisine> findByNames(List<String> names);

    @Query("SELECT c.name FROM Cuisine c WHERE c.name IN :cuisineList ")
    List<String> findExistingCuisineNamesInDB(@Param("cuisineList") Set<String> cuisineList);


}
