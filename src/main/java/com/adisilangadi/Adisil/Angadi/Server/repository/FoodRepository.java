package com.adisilangadi.Adisil.Angadi.Server.repository;

import com.adisilangadi.Adisil.Angadi.Server.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {

    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM Food f " +
            "LEFT JOIN f.foodCategory c " +
            "WHERE f.restaurant.id = :restaurantId " +
            "AND (:foodCategoryName IS NULL OR c.name = :foodCategoryName) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Food> searchAndFilterFoods(
            @Param("restaurantId") Long restaurantId,
            @Param("foodCategoryName") String foodCategoryName,
            @Param("keyword") String keyword
    );


    @Query("SELECT f FROM Food f " +
            "LEFT JOIN f.foodCategory c " +
            "WHERE f.restaurant.id = :restaurantId " +
            "AND (:foodCategoryName IS NULL OR c.name = :foodCategoryName) " +
            "AND (:isVegetarian IS NULL OR f.isVegetarian = :isVegetarian) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Food> searchFoodsAndFilterIsVegetarian(
            @Param("restaurantId") Long restaurantId,
            @Param("foodCategoryName") String foodCategoryName,
            @Param("isVegetarian") Boolean isVegetarian,
            @Param("keyword") String keyword
    );


    @Query("SELECT f FROM Food f " +
            "LEFT JOIN f.foodCategory c " +
            "WHERE f.restaurant.id = :restaurantId " +
            "AND (:foodCategoryName IS NULL OR c.name = :foodCategoryName) " +
            "AND (:isSeasonal IS NULL OR f.isSeasonal = :isSeasonal) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Food> searchFoodsAndFilterIsSeasonal(
            @Param("restaurantId") Long restaurantId,
            @Param("foodCategoryName") String foodCategoryName,
            @Param("isSeasonal") Boolean isSeasonal,
            @Param("keyword") String keyword
    );

//    @Query("""
//        SELECT f
//        FROM Food f
//        WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//           OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
//           OR LOWER(f.foodCategory.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//    """)
//    List<Food> searchFood(@Param("keyword") String keyword);
//
//
//    @Query("""
//    SELECT f
//    FROM Food f
//    WHERE f.restaurant.id = :restaurantId
//      AND (:isVegetarian IS NULL OR f.isVegetarian = :isVegetarian)
//      AND (:isSeasonal IS NULL OR f.isSeasonal = :isSeasonal)
//""")
//    List<Food> filterFoods(@Param("restaurantId") Long restaurantId, @Param("isVegetarian") Boolean isVegetarian, @Param("isSeasonal") Boolean isSeasonal);
//    @Param("categoryId") Long categoryId
//    AND (:categoryId IS NULL OR f.foodCategory.id = :categoryId)
}
