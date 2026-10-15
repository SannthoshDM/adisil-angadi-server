package com.adisilangadi.Adisil.Angadi.Server.repository;

import com.adisilangadi.Adisil.Angadi.Server.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory,Long> {

    List<IngredientCategory> findByRestaurantId(Long restaurantId);
}
