package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Order;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.service.OrderService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;

    public AdminOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders/{restaurantId}")
    public ResponseEntity<?> getRestaurantOrders(@PathVariable Long restaurantId,
                                                 @RequestParam(required = false) String orderStatus) throws Exception
    {
        try
        {
            List<Order> orders = orderService.getRestaurantOrders(restaurantId,orderStatus);
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orders not found");
        }
    }

    @PutMapping("/order/{orderId}/{orderStatus}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId,
                                                 @PathVariable String orderStatus) throws Exception
    {
        try
        {
            System.out.println("INSIDE UPDATE ORDER STATUS ......................");
            System.out.println("ORDER ID :"+orderId+" \nORDER STATUS : "+orderStatus);
            Order order = orderService.updateOrder(orderId,orderStatus);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to process the request");
        }
    }
}
