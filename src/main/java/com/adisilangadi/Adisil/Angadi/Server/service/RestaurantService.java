package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.dto.RestaurantDto;
import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.repository.AddressRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.CategoryRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.RestaurantRepository;
import com.adisilangadi.Adisil.Angadi.Server.repository.UserRepository;
import com.adisilangadi.Adisil.Angadi.Server.request.CreateRestaurantRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
//    private UserService userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public RestaurantService(RestaurantRepository restaurantRepository,AddressRepository addressRepository,UserRepository userRepository,CategoryRepository categoryRepository)
    {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Restaurant createRestaurant(CreateRestaurantRequest restaurantRequest, User user)
    {
        Address address = addressRepository.save(restaurantRequest.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
//        i have a doubt in setting contact information
        restaurant.setContactInformation(restaurantRequest.getContactInformation());
        restaurant.setCuisineType(restaurantRequest.getCuisineType());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setImages(restaurantRequest.getImages());
        restaurant.setName(restaurantRequest.getName());
        restaurant.setOpeningHours(restaurantRequest.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        restaurant.setOpen(true);


        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant,User user) throws Exception
    {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if(user.getId().equals(restaurant.getOwner().getId()))
        {
            restaurant.setName(updatedRestaurant.getName());
            restaurant.setDescription(updatedRestaurant.getDescription());
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());

            return restaurantRepository.save(restaurant);
        }
        else {
            throw new Exception("Restaurant not found");
        }
    }

    public void deleteRestaurant(Long restaurantId,User user) throws Exception
    {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if(user.getId().equals(restaurant.getOwner().getId()))
        {
            restaurantRepository.delete(restaurant);
        }
        else {
            throw new Exception("Restaurant not found");
        }
    }

    public List<Restaurant> getAllRestaurant()
    {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> searchRestaurant(String keyword)
    {
          return restaurantRepository.searchRestaurants(keyword);
    }

    public Restaurant findRestaurantById(Long id) throws Exception
    {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if(restaurant.isEmpty())
        {
            throw new Exception("Restaurant not found with id : "+ id);
        }
        return restaurant.get();
    }

    public Restaurant getRestaurantByUserId(Long userId) throws Exception
    {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if(restaurant == null)
        {
            throw new Exception("Restaurant not found with user id : "+ userId);
        }

        return restaurant;
    }

    public RestaurantDto addToFavorites(Long restaurantId,User user) throws Exception
    {
       Restaurant restaurant = findRestaurantById(restaurantId);

       RestaurantDto dto = new RestaurantDto();
       dto.setId(restaurantId);
       dto.setTitle(restaurant.getName());
       dto.setDescription(restaurant.getDescription());
       dto.setImages(restaurant.getImages());
       dto.setOpen(restaurant.isOpen());

//       if(user.getFavorites().contains(dto))
//       {
//           user.getFavorites().remove(dto);
//       }
//       else
//       {
//           user.getFavorites().add(dto);
//       }
        boolean isFavorited = false;
        List<RestaurantDto> favorites = user.getFavorites();
        for(RestaurantDto favorite : favorites)
        {
            if(favorite.getId().equals(restaurantId))
            {
                isFavorited = true;
                break;
            }
        }

        if(isFavorited)
        {
            favorites.removeIf(favorite -> favorite.getId().equals((restaurantId)));
        }
        else
        {
            favorites.add(dto);
        }
        user.setFavorites(favorites);

       userRepository.save(user);

       return dto;
    }

    public Restaurant updateRestaurantStatus(Long id,User user) throws Exception
    {
        Restaurant restaurant = findRestaurantById(id);
        if(user.getId().equals(restaurant.getOwner().getId()))
        {
            restaurant.setOpen(!restaurant.isOpen());

            return restaurantRepository.save(restaurant);
        }
        else {
            throw new Exception("Restaurant not found");
        }
    }

    public List<Category> findAllCategoriesByRestaurantId(Long restaurantId) throws Exception
    {
        try
        {
            return categoryRepository.findByRestaurantId(restaurantId);
        } catch (Exception e) {
            throw new Exception("Category not found");
        }
    }


}
