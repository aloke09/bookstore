package com.bridgelabz.cartms2.client;


import com.bridgelabz.cartms2.external.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MICROSERVICE1")
public interface UserServiceClient
{
    @GetMapping("user/get")
    User getUser(@RequestParam("authHeader") String authHeader);
}