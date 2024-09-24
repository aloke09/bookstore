package com.bridgelabz.user_microservice1.service;

import com.bridgelabz.user_microservice1.DTO.UserDTO;
import com.bridgelabz.user_microservice1.model.User;

public interface UserService
{
    UserDTO registerUser(User user);
    String login(User user);
    String forgotPassword(String email);
    String resetPassword(String token, String newPassword);
    UserDTO getUser(String token);

}
