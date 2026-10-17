package com.adisilangadi.Adisil.Angadi.Server.controller;

import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import com.adisilangadi.Adisil.Angadi.Server.model.User;
import com.adisilangadi.Adisil.Angadi.Server.request.AddUserAddressRequest;
import com.adisilangadi.Adisil.Angadi.Server.service.JwtService;
import com.adisilangadi.Adisil.Angadi.Server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService,JwtService jwtService)
    {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome()
    {
        return ResponseEntity.status(HttpStatus.OK).body("User welcome page");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            User user = userService.findUserByJwtToken(authorization);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @PostMapping("/address")
    public ResponseEntity<?> addUserAddress(@RequestBody AddUserAddressRequest userAddress, @RequestHeader("Authorization") String authorization) throws Exception
    {
        try {
            System.out.println("INSIDE ADD USER ADDRESS ...................");
            System.out.println("REQUESTED USER ADDRESS : "+userAddress.getAddress().toString());
            User user = userService.findUserByJwtToken(authorization);
            Address savedAddress = userService.addUserAddress(userAddress.getAddress(),user);
            return ResponseEntity.status(HttpStatus.OK).body(savedAddress);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }
}
