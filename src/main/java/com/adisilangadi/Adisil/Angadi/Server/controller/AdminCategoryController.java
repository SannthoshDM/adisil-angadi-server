package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.service.CategoryService;
import com.adisilangadi.Adisil.Angadi.Server.service.RestaurantService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// THIS CONTROLLER IS FOR FOOD CATEGORY - WHICH IS MANAGED BY THE ADMIN OR RESTAURANT_OWNER

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private CategoryService categoryService;
    private UserService userService;
    private final RestaurantService restaurantService;

    public AdminCategoryController(CategoryService categoryService,UserService userService,RestaurantService restaurantService)
    {
        this.categoryService = categoryService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category,
                                            @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Category newCategory = categoryService.createCategory(category.getName(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getCategoriesByRestaurant(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
            List<Category> categories = categoryService.findCategoriesByRestaurantId(restaurant.getId());
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId,@RequestBody Category category) throws Exception
    {
        try {
            Category updateCategory = categoryService.findCategoryById(categoryId);
            updateCategory.setName(category.getName());
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(updateCategory));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
