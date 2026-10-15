package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Cart;
import com.adisilangadi.Adisil.Angadi.Server.model.CartItem;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.request.AddCartItemRequest;
import com.adisilangadi.Adisil.Angadi.Server.request.UpdateCartItemRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.CartService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService,UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createCartForUser(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);

            Cart cart = cartService.createCartByUser(user);

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create cart for the user");
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserCart(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);

            Cart cart = cartService.findCartByUserId(user.getId());

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add item to the cart");
        }
    }

    @PutMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody AddCartItemRequest cartItemRequest, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            Cart cart = cartService.addItemToCart(cartItemRequest,authorization);

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add item to the cart");
        }
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody UpdateCartItemRequest cartItemRequest) throws Exception
    {
        try {
            Cart cart = cartService.updateCartItemQuantity(cartItemRequest.getCartItemId(),cartItemRequest.getQuantity());

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update item into the cart");
        }
    }

    @DeleteMapping("/cart-item/{cartItemId}/remove")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId,@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            Cart cart = cartService.removeItemFromCart(cartItemId,authorization);

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to remove item from the cart");
        }
    }

    @PutMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);

            Cart cart = cartService.clearCart(user.getId());

            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to clear cart");
        }
    }


}
