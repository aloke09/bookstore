package com.bridgelabz.user_microservice1.controller;

import com.bridgelabz.user_microservice1.DTO.UserDTO;
import com.bridgelabz.user_microservice1.model.User;
import com.bridgelabz.user_microservice1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public UserDTO register(@RequestBody User user)
    {
        return service.registerUser(user);
    }

    @GetMapping("/login")
    public String login(@RequestBody User user)
    {
        return service.login(user);
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody Map<String, String> request)
    {
        String email = request.get("email");
        return service.forgotPassword(email);
    }

    @PostMapping("/reset")
    public String reset(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> request)
    {
        String password = request.get("newpassword");

        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);
            return service.resetPassword(token, password);
        }
        else
        {
            return "Authorization header must contain a Bearer token";
        }
    }

//    @GetMapping("/get")
//    public ResponseEntity<UserDTO> getUser(@RequestParam("authHeader") String authHeader)
//    {
//        System.out.println(authHeader);
//        log.warn("warning!!!");
//        if (authHeader != null && authHeader.startsWith("Bearer "))
//        {
//            String token = authHeader.substring(7);
//            UserDTO u=service.getUser(token);
//            if(u!=null)
//            {
//                System.out.println(u.getId());
//                return new ResponseEntity<>(u, HttpStatus.OK);
//            }
//            else
//            {
//                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
//            }
//
//        }
//        else
//        {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//    }

    @GetMapping("/get")
    public ResponseEntity<UserDTO> getUser(@RequestParam("authHeader") String authHeader) {
        log.info("Received authHeader: {}", authHeader);
        log.warn("Warning!!!");

        // Validate authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UserDTO userDTO = service.getUser(token);

            if (userDTO != null) {
                log.info("User ID: {}", userDTO.getId());
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }
            else
            {
                log.warn("User not found for token: {}", token);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } else
        {
            log.error("Invalid or missing Authorization header");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/check")
    public String check()
    {
        return "working!!";
    }
}
