package com.adisilangadi.Adisil.Angadi.Server.repository;

import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    public Restaurant findByOwnerId(Long userId);

    public List<Restaurant> findByName(String name);

    @Query("""
        SELECT r
        FROM Restaurant r
        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Restaurant> searchRestaurants(@Param("keyword") String keyword);

}
