package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;

    public CategoryService(CategoryRepository categoryRepository,RestaurantService restaurantService) {
        this.categoryRepository = categoryRepository;
        this.restaurantService = restaurantService;
    }

    public Category createCategory(String name,Long userId) throws Exception
    {
        try
        {
            Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
            Category category = new Category();
            category.setName(name);
            category.setRestaurant(restaurant);
            return categoryRepository.save(category);
        }
        catch (Exception e)
        {
            throw new Exception("Unable to create category");
        }
    }

    public List<Category> findCategoriesByRestaurantId(Long id) throws Exception
    {
        try
        {
            return categoryRepository.findByRestaurantId(id);
        } catch (Exception e) {
            throw new Exception("Category not found");
        }
    }

    public Category findCategoryById(Long id) throws Exception
    {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty())
        {
            throw new Exception("Category not found");
        }
        return category.get();
    }

    public Category updateCategory(Category category)
    {
        return categoryRepository.save(category);
    }


}
