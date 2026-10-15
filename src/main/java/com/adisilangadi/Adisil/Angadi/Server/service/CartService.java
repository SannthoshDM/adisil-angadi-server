package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.Cart;
import com.adisilangadi.Adisil.Angadi.Server.model.CartItem;
import com.adisilangadi.Adisil.Angadi.Server.model.Food;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.repository.CartItemRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.CartRepository;
import com.adisilangadi.Adisil.Angadi.Server.request.AddCartItemRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

// HERE BOTH CART AND CART_ITEM SERVICE ARE THERE
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final FoodService foodService;

    public CartService(CartRepository cartRepository,CartItemRepository cartItemRepository,UserService userService,FoodService foodService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.foodService = foodService;
    }

    // CART_ITEM
    public Cart addItemToCart(AddCartItemRequest cartItemRequest, String authorization) throws Exception
    {
        User user = userService.findUserByJwtToken(authorization);

        Food food = foodService.findFoodById(cartItemRequest.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem : cart.getItem())
        {
            if(cartItem.getFood().equals(food))
            {
                int newQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();
                return updateCartItemQuantity(cartItem.getId(),newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(cartItemRequest.getQuantity());
        newCartItem.setIngredients(cartItemRequest.getIngredients());
        newCartItem.setTotalPrice(cartItemRequest.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        cart.getItem().add(savedCartItem);

        // here after adding the item to the cart, so we have to update the total price of that cart
        // so use calculateCartTotals() methods to find the correct price;

        // please check carefully.....
        Long totalPriceOfCart = calculateCartTotals(cart);
        cart.setTotal(totalPriceOfCart);

        return cartRepository.save(cart);
    }

    public Cart updateCartItemQuantity(Long cartItemId,int quantity) throws Exception
    {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if(cartItem.isEmpty())
        {
            throw new Exception("Cart item not found");
        }

        CartItem newCartItem = cartItem.get();
        newCartItem.setQuantity(quantity);
        Long foodPrice = newCartItem.getFood().getPrice();
        newCartItem.setTotalPrice(foodPrice * quantity);

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        // here after updating quantity of the item from the cart, so we have to update the total price of that cart
        // so use calculateCartTotals() methods to find the correct price;

//        please check this carefully.......
        Cart cart = cartItem.get().getCart();
        Long totalPriceOfCart = calculateCartTotals(cart);
        cart.setTotal(totalPriceOfCart);

        return cartRepository.save(cart);

    }


    // CART
    public Cart removeItemFromCart(Long cartItemId, String authorization) throws Exception
    {
        User user = userService.findUserByJwtToken(authorization);

        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if(cartItem.isEmpty())
        {
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItem.get();
        cart.getItem().remove(item);

        // here after removing the item from the cart, so we have to update the total price of that cart
        // so use calculateCartTotals() methods to find the correct price;
        // please check carefully.....
        Long totalPriceOfCart = calculateCartTotals(cart);
        cart.setTotal(totalPriceOfCart);

        return cartRepository.save(cart);
    }

    public Long calculateCartTotals(Cart cart) throws Exception
    {
        Long total = 0L;

        for(CartItem cartItem : cart.getItem())
        {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }

        return total;
    }

    public Cart findCartById(Long cartId) throws Exception
    {
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (cart.isEmpty())
        {
            throw new Exception("Cart not Found with id :"+cartId);
        }

        return cart.get();
    }

    public Cart findCartByUserId(Long userId) throws Exception
    {
        Cart cart = cartRepository.findByCustomerId(userId);
//        cart.setTotal(calculateCartTotals(cart));
        return cart;
    }

    public Cart clearCart(Long userId) throws Exception
    {

        Cart cart = findCartByUserId(userId);

        cart.getItem().clear();

        // here we are also setting total price of the cart to 0.
        cart.setTotal(0L);

        return cartRepository.save(cart);
    }

    public Cart createCartByUser(User user) throws Exception
    {
        Cart newCart = new Cart();
        newCart.setCustomer(user);
        return cartRepository.save(newCart);
    }

}
