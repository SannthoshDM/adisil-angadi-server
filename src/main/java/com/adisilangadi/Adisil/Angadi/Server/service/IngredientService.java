package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.IngredientCategory;
import com.adisilangadi.Adisil.Angadi.Server.model.IngredientsItem;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.repository.IngredientCategoryRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.IngredientsItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// HERE BOTH INGREDIENT_CATEGORY AND INGREDIENT_ITEMS SERVICES ARE THERE
@Service
public class IngredientService {

    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientsItemRepository ingredientsItemRepository;
    private final RestaurantService restaurantService;

    public IngredientService(IngredientCategoryRepository ingredientCategoryRepository,IngredientsItemRepository ingredientsItemRepository,RestaurantService restaurantService) {
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.ingredientsItemRepository = ingredientsItemRepository;
        this.restaurantService = restaurantService;
    }


    // INGREDIENT_CATEGORY

    public IngredientCategory createIngredientCategory(String ingredientCategoryName,Long restaurantId) throws Exception
    {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory ingredientCategory = new IngredientCategory();

        ingredientCategory.setRestaurant(restaurant);
        ingredientCategory.setName(ingredientCategoryName);

        return ingredientCategoryRepository.save(ingredientCategory);

    }

    public IngredientCategory findIngredientCategoryById(Long ingredientCategoryId) throws  Exception
    {
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId);

        if(ingredientCategory.isEmpty())
        {
            throw new Exception("Ingredient Category not found");
        }
        return ingredientCategory.get();
    }

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws  Exception
    {
        restaurantService.findRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }


    // INGREDIENT_ITEMS

    public IngredientsItem createIngredientItem(Long restaurantId,String ingredientName, Long ingredientCategoryId) throws Exception
    {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory ingredientCategory = findIngredientCategoryById(ingredientCategoryId);

        IngredientsItem ingredientsItem = new IngredientsItem();

        ingredientsItem.setName(ingredientName);
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setCategory(ingredientCategory);

        IngredientsItem savedIngredientsItem = ingredientsItemRepository.save(ingredientsItem);
        ingredientCategory.getIngredientsItems().add(savedIngredientsItem);

        return savedIngredientsItem;
    }

    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId)
    {
        return ingredientsItemRepository.findByRestaurantId(restaurantId);
    }

    public IngredientsItem updateStock(Long ingredientId) throws Exception
    {
        Optional<IngredientsItem> ingredientsItem = ingredientsItemRepository.findById(ingredientId);

        if(ingredientsItem.isEmpty())
        {
            throw new Exception("Ingredient not found");
        }

        IngredientsItem updatedIngredientsItem = ingredientsItem.get();
        updatedIngredientsItem.setInStock(!updatedIngredientsItem.isInStock());
        return ingredientsItemRepository.save(updatedIngredientsItem);
    }

}
