package com.bridgelabz.user_microservice1.controller;

import com.bridgelabz.user_microservice1.DTO.UserDTO;
import com.bridgelabz.user_microservice1.model.User;
import com.bridgelabz.user_microservice1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/check")
    public String check()
    {
        return "working!!";
    }
}
