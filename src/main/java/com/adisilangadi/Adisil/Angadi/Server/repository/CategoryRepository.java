package com.adisilangadi.Adisil.Angadi.Server.repository;

import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    public List<Category> findByRestaurantId(Long id);
}
