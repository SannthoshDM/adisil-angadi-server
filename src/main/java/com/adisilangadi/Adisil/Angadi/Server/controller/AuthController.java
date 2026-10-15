package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.USER_ROLE;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.response.AuthResponse;
import com.adisilangadi.Adisil.Angadi.Server.request.LoginRequest;
import com.adisilangadi.Adisil.Angadi.Server.request.SignupRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.CartService;
import com.adisilangadi.Adisil.Angadi.Server.service.JwtService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CartService cartService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, CartService cartService)
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cartService = cartService;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request)
    {
        System.out.println("Inside Signup request : "+request.toString());

        try {
            if (request.getName() == null
                    || request.getName().isBlank()
                    || request.getEmail() == null
                    || request.getEmail().isBlank()
                    || request.getPassword() == null
                    || request.getPassword().isBlank()
                    || request.getRole() == null
                    || request.getRole().isBlank()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Name, email, password and role are required");
            }

            String email = request.getEmail().trim().toLowerCase();

            if (userService.existsByEmail(email)) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already registered");
            }

            User savedUser = userService.addUser(request, email);

            if(savedUser.getRole().equals(USER_ROLE.ROLE_CUSTOMER.toString()))
            {
                System.out.println("SAVED USER ROLE : ROLE_CUSTOMER");
                System.out.println("Creating Cart for the new user .....");
                cartService.createCartByUser(savedUser);
            }

            System.out.println("CREATING LOGIN REQUEST .........");
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(request.getPassword());

            return login(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to process the request");
        }
    }


    // Removed the role checks here as they are already managed in SecurityConfig

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        System.out.println("Inside Login request :"+request.toString());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail().trim().toLowerCase(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("");

        AuthResponse response = new AuthResponse(
                token,
                userDetails.getUsername(),
                role
        );

        return ResponseEntity.ok(response);
    }

}
