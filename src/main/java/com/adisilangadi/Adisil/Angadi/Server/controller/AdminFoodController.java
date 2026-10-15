package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Food;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.request.CreateFoodRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.FoodService;
import com.adisilangadi.Adisil.Angadi.Server.service.RestaurantService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// THIS CONTROLLER IS FOR FOOD  - WHICH IS MANAGED BY THE ADMIN OR RESTAURANT_OWNER

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public AdminFoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping()
    public ResponseEntity<?> getFoodByRestaurant(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
            List<Food> foods = foodService.getFoodByRestaurantId(restaurant.getId());
            return ResponseEntity.status(HttpStatus.OK).body(foods);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.OK).body("No Foods found");
        }
    }

    @PostMapping()
    public ResponseEntity<?> createFood(@RequestBody CreateFoodRequest foodRequest, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.findRestaurantById(foodRequest.getRestaurantId());
            Food food = foodService.createFood(foodRequest,foodRequest.getCategory(),restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(food);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }

    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<?> deleteFood(@PathVariable Long foodId, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try
        {
            User user = userService.findUserByJwtToken(authorization);
            foodService.deleteFood(foodId);
            return ResponseEntity.status(HttpStatus.OK).body("Food Deleted Successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchFood( @RequestHeader("Authorization") String authorization) throws Exception
//    {
//        User user = userService.findUserBYJwtToken(authorization);
//
//        return ResponseEntity.status(HttpStatus.OK).body(new Food());
//    }

    @PutMapping("/{foodId}/updateFoodAvailability")
    public ResponseEntity<?> updateFoodAvailabilityStatus(@PathVariable Long foodId, @RequestHeader("Authorization") String authorization) throws Exception
    {
        User user = userService.findUserByJwtToken(authorization);
        Food food = foodService.updateFoodAvailabilityStatus(foodId);
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

}
