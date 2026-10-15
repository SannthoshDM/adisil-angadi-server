package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Food;
import com.adisilangadi.Adisil.Angadi.Server.service.FoodService;
import com.adisilangadi.Adisil.Angadi.Server.service.RestaurantService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/foods")
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

//    ALL FOODS ARE NOT REQUIRED FOR USERS , ONLY PARTICULAR RESTAURANT FOOD IS NEEDED
//    @GetMapping()
//    public ResponseEntity<?> getAllFoods() throws Exception
//    {
//        try
//        {
//            List<Food> foods = foodService.getAllFoods();
//            return ResponseEntity.status(HttpStatus.OK).body(foods);
//        }
//        catch (Exception e)
//        {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
//        }
//    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantFoods(@PathVariable Long restaurantId) throws Exception
    {
        try
        {
            System.out.println("**************");
            List<Food> foods = foodService.getFoodByRestaurantId(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body(foods);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
        }
    }

    @GetMapping("/restaurant/{restaurantId}/isVegetarian")
    public ResponseEntity<?> filterRestaurantFoodsByVegetarian(@PathVariable Long restaurantId,
                                                   @RequestParam boolean isVegetarian,
                                                   @RequestParam(required = false) String foodCategory,
                                                   @RequestParam(required = false) String keyword) throws Exception
    {
        try
        {
            System.out.println("Inside isVegetarian ////////////////////////////");
            List<Food> foods = foodService.searchFoodsAndFilterIsVegetarian(restaurantId,foodCategory,isVegetarian,keyword);
            return ResponseEntity.status(HttpStatus.OK).body(foods);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
        }
    }

    @GetMapping("/restaurant/{restaurantId}/isSeasonal")
    public ResponseEntity<?> filterRestaurantFoodsBySeasonal(@PathVariable Long restaurantId,
                                                   @RequestParam boolean isSeasonal,
                                                   @RequestParam(required = false) String foodCategory,
                                                   @RequestParam(required = false) String keyword) throws Exception
    {
        try
        {
            System.out.println("Inside isSeasonal >>>>>>>>>>>>>>>>>>>>>");
            List<Food> foods = foodService.searchFoodsAndFilterIsSeasonal(restaurantId,foodCategory,isSeasonal,keyword);
            return ResponseEntity.status(HttpStatus.OK).body(foods);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
        }
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchFoods(@RequestParam String keyword) throws Exception
//    {
//        try
//        {
//            List<Food> foods = foodService.searchFood(keyword);
//            return ResponseEntity.status(HttpStatus.OK).body(foods);
//        }
//        catch (Exception e)
//        {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
//        }
//    }

    @GetMapping("/restaurant/{restaurantId}/search")
    public ResponseEntity<?> filterRestaurantFoods(@PathVariable Long restaurantId,
                                                   @RequestParam(required = false) String foodCategory,
                                                   @RequestParam(required = false) String keyword) throws Exception
    {
        try
        {
            System.out.println("inside all - search ........................");
            List<Food> foods = foodService.searchAndFilterFoods(restaurantId,foodCategory,keyword);
            return ResponseEntity.status(HttpStatus.OK).body(foods);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Food Available");
        }
    }

}
