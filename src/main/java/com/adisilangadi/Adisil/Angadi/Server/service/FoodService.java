package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.Food;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.repository.FoodRepository;
import com.adisilangadi.Adisil.Angadi.Server.request.CreateFoodRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository)
    {
        this.foodRepository = foodRepository;
    }

    public Food createFood(CreateFoodRequest foodRequest, Category category, Restaurant restaurant)
    {
        Food newFood = new Food();
        newFood.setFoodCategory(category);
        newFood.setRestaurant(restaurant);
        newFood.setDescription(foodRequest.getDescription());
        newFood.setImages(foodRequest.getImages());
        newFood.setName(foodRequest.getName());
        newFood.setPrice(foodRequest.getPrice());
        newFood.setIngredients(foodRequest.getIngredientsItems());
        newFood.setSeasonal(foodRequest.isSeasonal());
        newFood.setVegetarian(foodRequest.isVegetarian());
        newFood.setAvailable(true);
        newFood.setCreationDate(new Date());

        Food savedFood = foodRepository.save(newFood);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }


    public void deleteFood(Long foodId) throws Exception
    {
        Food food = findFoodById(foodId);
        food.setRestaurant(null);
        foodRepository.delete(food);
    }

//    public List<Food> filterRestaurantFoods(Long restaurantId, boolean isVegetarian, boolean isSeasonal,String foodCategory)
//    {
//
//        System.out.println("===========================");
//        List<Food> foods = foodRepository.filterFoods(restaurantId,isVegetarian,isSeasonal);
//        System.out.println("===========================");
//        if(foodCategory != null && !foodCategory.isBlank())
//        {
//            foods.stream().filter(food -> {
//                if(food.getFoodCategory()!= null)
//                {
//                    return food.getFoodCategory().getName().equals(foodCategory);
//                }
//                return false;
//            }).collect(Collectors.toList());
//        }
//        return foods;
//    }

    public List<Food> searchAndFilterFoods(Long restaurantId, String foodCategoryName,String keyword)
    {
        System.out.println("inside searchAndFilterFoods ........................ ");
        if(foodCategoryName.equals("null") || foodCategoryName == null || foodCategoryName.isEmpty())
        {
            System.out.println(".................. "+foodCategoryName);
            foodCategoryName = null;
        }
        if(keyword.equals("null") || keyword == null || keyword.isEmpty() )
        {
            System.out.println(",,,,,,,,,,,,,,,,,,,, "+keyword);
            keyword = null;
        }
        return foodRepository.searchAndFilterFoods(restaurantId,foodCategoryName,keyword);
    }

    public List<Food> searchFoodsAndFilterIsVegetarian(Long restaurantId, String foodCategoryName,boolean isVegetarian,String keyword)
    {
        System.out.println("inside searchFoodsAndFilterIsVegetarian ///////////////////");
        if(foodCategoryName.equals("null") || foodCategoryName == null || foodCategoryName.isEmpty())
        {
            System.out.println(".................. "+foodCategoryName);
            foodCategoryName = null;
        }
        if(keyword.equals("null") || keyword == null || keyword.isEmpty() )
        {
            System.out.println(",,,,,,,,,,,,,,,,,,,, "+keyword);
            keyword = null;
        }
        return foodRepository.searchFoodsAndFilterIsVegetarian(restaurantId,foodCategoryName,isVegetarian,keyword);
    }


    public List<Food> searchFoodsAndFilterIsSeasonal(Long restaurantId, String foodCategoryName,boolean isSeasonal,String keyword)
    {
        System.out.println("inside searchFoodsAndFilterIsSeasonal >>>>>>>>>>>>>>>>>>>>>>>>");
        if(foodCategoryName.equals("null") || foodCategoryName == null || foodCategoryName.isEmpty())
        {
            System.out.println(".................. "+foodCategoryName);
            foodCategoryName = null;
        }
        if(keyword.equals("null") || keyword == null || keyword.isEmpty() )
        {
            System.out.println(",,,,,,,,,,,,,,,,,,,, "+keyword);
            keyword = null;
        }
        return foodRepository.searchFoodsAndFilterIsSeasonal(restaurantId,foodCategoryName,isSeasonal,keyword);
    }

    public Food findFoodById(Long foodId) throws Exception
    {
        Optional<Food> food = foodRepository.findById(foodId);
        if(food.isEmpty())
        {
            throw new Exception("Food not found");
        }
        return food.get();
    }

    public Food updateFoodAvailabilityStatus(Long foodId) throws Exception
    {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }

    public List<Food> getFoodByRestaurantId(Long restaurantId)
    {
        return foodRepository.findByRestaurantId(restaurantId);
    }



}
