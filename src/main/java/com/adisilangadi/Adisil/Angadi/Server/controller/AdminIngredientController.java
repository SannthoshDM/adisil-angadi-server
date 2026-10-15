package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.IngredientCategory;
import com.adisilangadi.Adisil.Angadi.Server.model.IngredientsItem;
import com.adisilangadi.Adisil.Angadi.Server.request.IngredientCategoryRequest;
import com.adisilangadi.Adisil.Angadi.Server.request.IngredientsItemRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class AdminIngredientController {

    private final IngredientService ingredientService;

    public AdminIngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping("/category")
    public ResponseEntity<?> createIngredientCategory(@RequestBody IngredientCategoryRequest ingredientCategory) throws Exception
    {
        try {
            IngredientCategory ic = ingredientService.createIngredientCategory(ingredientCategory.getName(),ingredientCategory.getRestaurantId());
            return ResponseEntity.status(HttpStatus.CREATED).body(ic);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }
    }


    @PostMapping
    public ResponseEntity<?> createIngredientsItem(@RequestBody IngredientsItemRequest ingredientsItems) throws Exception
    {
        try {
            IngredientsItem items = ingredientService.createIngredientItem(ingredientsItems.getRestaurantId(),ingredientsItems.getName(),ingredientsItems.getIngredientCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED).body(items);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }
    }

    @PutMapping("/{ingredientItemId}/updateStock")
    public ResponseEntity<?> updateIngredientItemStock(@PathVariable Long ingredientItemId) throws Exception
    {
        try {
            IngredientsItem item = ingredientService.updateStock(ingredientItemId);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantIngredients(@PathVariable Long restaurantId) throws Exception
    {
        try {
            List<IngredientsItem> items = ingredientService.findRestaurantsIngredients(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body(items);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/restaurant/{restaurantId}/category")
    public ResponseEntity<?> getRestaurantIngredientCategory(@PathVariable Long restaurantId) throws Exception
    {
        try {
            List<IngredientCategory> ingredientCategories = ingredientService.findIngredientCategoryByRestaurantId(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body(ingredientCategories);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient Category not found");
        }
    }
}
