package com.bridgelabz.book_microservice1.client;

import com.bridgelabz.book_microservice1.external.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MICROSERVICE1")
public interface UserServiceClient
{
    @GetMapping("user/get")
    User getUser(@RequestParam("authHeader") String authHeader);
//    User getUser(@RequestHeader("Authorization") String authHeader);

}
