package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.dto.RestaurantDto;
import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.service.RestaurantService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    public RestaurantController(RestaurantService restaurantService,UserService userService)
    {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllRestaurant(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            List<Restaurant> restaurant = restaurantService.getAllRestaurant();
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRestaurantBySearch(@RequestHeader("Authorization") String authorization,@RequestParam("keyword") String keyword) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword.trim());
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@RequestHeader("Authorization") String authorization, @PathVariable Long restaurantId) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @PutMapping("/{restaurantId}/add-favourites")
    public ResponseEntity<?> addToFavourites(@RequestHeader("Authorization") String authorization,@PathVariable Long restaurantId) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            RestaurantDto restaurantDto = restaurantService.addToFavorites(restaurantId,user);
            return ResponseEntity.status(HttpStatus.OK).body(restaurantDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/{restaurantId}/categories")
    public ResponseEntity<?> getRestaurantCategoriesById(@PathVariable Long restaurantId,@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            List<Category> categories = restaurantService.findAllCategoriesByRestaurantId(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

}
