package com.adisilangadi.Adisil.Angadi.Server.service;

import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import com.adisilangadi.Adisil.Angadi.Server.repository.AddressRepository;
import com.adisilangadi.Adisil.Angadi.Server.request.SignupRequest;
import com.adisilangadi.Adisil.Angadi.Server.model.USER_ROLE;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AddressRepository addressRepository;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder,JwtService jwtService,AddressRepository addressRepository) {
        this.userRepository = repository;
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
        this.addressRepository = addressRepository;
    }


    // Add any additional methods for registering or managing users
    public User addUser(SignupRequest request,String email) throws Exception
    {

        System.out.println("Inside addUser() method ......");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);

        // Never store the raw password.
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Public signup always creates a customer.
        if(request.getRole().equals(USER_ROLE.ROLE_CUSTOMER.toString()))
        {
            System.out.println("ROLE_CUSTOMER");
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
        }
        else if(request.getRole().equals(USER_ROLE.ROLE_RESTAURANT_OWNER.toString()))
        {
            System.out.println("ROLE_RESTAURANT_OWNER");
            user.setRole(USER_ROLE.ROLE_RESTAURANT_OWNER);
        }
        else if(request.getRole().equals(USER_ROLE.ROLE_ADMIN.toString())){
            System.out.println("ROLE_ADMIN");
            user.setRole(USER_ROLE.ROLE_ADMIN);
        }

        User savedUser = userRepository.save(user);

        System.out.println("User saved successfully .........");

        return savedUser;
    }

    public User findUserByJwtToken(String authorization) throws Exception
    {
        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String auth_token = authorization.substring(7);
                String user_email = jwtService.extractUsername(auth_token);

                return userRepository.findByEmail(user_email);
            }
            throw new Exception("Unauthorized request : User not found");
        }
        catch (Exception e)
        {
            throw new Exception("Unauthorized request : User not found");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public Address addUserAddress(Address userAddress,User user) throws Exception
    {
        try
        {
            System.out.println("INSIDE ADD USER ADDRESS ...................");
            Address savedAddress = addressRepository.save(userAddress);

            if(!user.getAddresses().contains(savedAddress))
            {
                user.getAddresses().add(savedAddress);
                userRepository.save(user);
            }

            return savedAddress;
        } catch (Exception e) {
            throw new Exception("Unable to add User Address...");
        }
    }
}
