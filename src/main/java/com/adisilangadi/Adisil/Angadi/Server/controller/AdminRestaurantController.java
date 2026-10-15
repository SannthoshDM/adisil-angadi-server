package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.request.CreateRestaurantRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.RestaurantService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurant")
public class AdminRestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    public AdminRestaurantController(RestaurantService restaurantService,UserService userService)
    {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getRestaurantByUser(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createRestaurant(@RequestBody CreateRestaurantRequest restaurantRequest, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.createRestaurant(restaurantRequest,user);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(@RequestBody CreateRestaurantRequest restaurantRequest, @RequestHeader("Authorization") String authorization, @PathVariable Long restaurantId) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.updateRestaurant(restaurantId,restaurantRequest,user);
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> deleteRestaurant(@RequestHeader("Authorization") String authorization, @PathVariable("restaurantId") Long restaurantId) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            restaurantService.deleteRestaurant(restaurantId,user);
            return ResponseEntity.status(HttpStatus.OK).body("Restaurant Deleted Successfully......");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @PutMapping("/{restaurantId}/status")
    public ResponseEntity<?> updateRestaurantStatus(@RequestHeader("Authorization") String authorization, @PathVariable("restaurantId") Long restaurantId) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Restaurant restaurant = restaurantService.updateRestaurantStatus(restaurantId,user);
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }
}
