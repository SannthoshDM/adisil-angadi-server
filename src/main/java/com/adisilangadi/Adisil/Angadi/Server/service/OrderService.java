package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.*;
import com.adisilangadi.Adisil.Angadi.Server.repository.AddressRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.OrderItemRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.OrderRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.UserRepository;
import com.adisilangadi.Adisil.Angadi.Server.request.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository,UserRepository userRepository,RestaurantService restaurantService,CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
        this.cartService = cartService;
    }

//    CREATING ORDER FROM USER SIDE
    public Order createOrder(OrderRequest orderRequest, User user) throws Exception
    {
        Optional<Address> deliveryAddress = addressRepository.findById(orderRequest.getAddressId());
//        Address savedAddress = addressRepository.save(shippingAddress);
//
//        if(!user.getAddresses().contains(savedAddress))
//        {
//            user.getAddresses().add(savedAddress);
//            userRepository.save(user);
//        }
        if(deliveryAddress.isEmpty())
        {
            throw new Exception("Address not found. Unable to Process the request");
        }

        Restaurant restaurant = restaurantService.findRestaurantById(orderRequest.getRestaurantId());

        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setDeliveryAddress(deliveryAddress.get());
        createdOrder.setRestaurant(restaurant);

        Cart cart = cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItem())
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        createdOrder.setItems(orderItems);

        int totalPrice = cart.getTotal().intValue();
        int totalItems = cart.getItem().size();

        createdOrder.setTotalAmount(orderRequest.getTotalBillAmount());
        createdOrder.setTotalPrice(totalPrice);
        createdOrder.setTotalItem(totalItems);

        Order savedOrder = orderRepository.save(createdOrder);

        restaurant.getOrders().add(savedOrder);

        user.getOrders().add(savedOrder);

//        one doubt ? here whether we want to save user and restaurant object in table or not?

//        After Placing Order , we have to clear the user cart
        cartService.clearCart(user.getId());

        return savedOrder;

    }

    public Order updateOrder(Long orderId,String orderStatus) throws Exception
    {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY")
                || orderStatus.equals("DELIVERED")
                || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING"))
        {
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Please select a valid order status");
    }

    public void cancelOrder(Long orderId) throws Exception
    {
        Order order = findOrderById(orderId);
        if(order == null)
        {
            throw new Exception("Unable to cancel the order - Order not found");
        }

        orderRepository.deleteById(orderId);
    }

    public List<Order> getUserOrders(Long userId) throws Exception
    {
        return orderRepository.findByCustomerId(userId);
    }

    public List<Order> getRestaurantOrders(Long restaurantId,String orderStatus) throws Exception
    {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if(orderStatus != null)
        {
            orders = orders.stream()
                    .filter(order ->
                            order.getOrderStatus().equals(orderStatus))
                    .collect(Collectors.toList());
        }

        return orders;
    }

    public Order findOrderById(Long orderId) throws Exception
    {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty())
        {
            throw new Exception("Order not found");
        }
        return order.get();
    }

}
