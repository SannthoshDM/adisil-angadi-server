package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Order;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.request.OrderRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.OrderService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try
        {
            User user = userService.findUserByJwtToken(authorization);
            List<Order> orders = orderService.getUserOrders(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orders not found");
        }
    }

    @PostMapping("/order")
    public ResponseEntity<?> createUserOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            Order order = orderService.createOrder(orderRequest,user);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable tp process Order request");
        }
    }
}
