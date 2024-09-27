package com.bridgelabz.user_microservice1.service;

import com.bridgelabz.user_microservice1.DTO.UserDTO;
import com.bridgelabz.user_microservice1.model.User;
import com.bridgelabz.user_microservice1.repository.UserRepository;
import com.bridgelabz.user_microservice1.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(User user)
    {
        UserDTO userDTO=new UserDTO();

        user.setRegistered_date(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);
        userDTO.setId(saved.getId());
        userDTO.setFirstName(saved.getFirstName());
        userDTO.setLastName(saved.getLastName());
        userDTO.setDob(saved.getDob());
        userDTO.setEmail(saved.getEmail());
        userDTO.setRole(saved.getRole());

        return userDTO;
    }

    @Override
    public String login(User user)
    {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword())
        );
        if(authentication.isAuthenticated())
        {
            String token = jwtService.generateToken(user.getEmail());
            return token;
        }
        return "Either E-mail or password is incorrect!!";
    }

    @Override
    public String forgotPassword(String email)
    {
        System.out.println(email);
        User byEmail = userRepository.findByEmail(email);
        if(byEmail==null)
        {
            return "E-mail not found!!";
        }
        String resetToken = jwtService.generateToken(email);
        System.out.println("Reset Token ---> "+resetToken);
        return resetToken;
    }

    @Override
    public String resetPassword(String token, String newPassword)
    {
        System.out.println("In reset password");

        if (newPassword == null || newPassword.trim().isEmpty())
        {
            return "New Password cannot be null or empty";
        }

        // Validate the token and extract the email
        String email = jwtService.extractUserName(token);
        System.out.println("Email: " + email);

        if (email == null || userRepository.findByEmail(email)== null) {
            return "Invalid token or email";
        }

        // Fetch the user
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "User not found";
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password successfully reset";
    }

//    @Override
//    public UserDTO getUser(String token)
//    {
//        System.out.println("In reset password");
//
//
//        // Validate the token and extract the email
//        String email = jwtService.extractUserName(token);
//        System.out.println("Email: " + email);
//
//        if (email == null || userRepository.findByEmail(email)== null)
//        {
//            System.out.println("Invalid token or email");
//            return null;
//        }
//
//        // Fetch the user
//        User user = userRepository.findByEmail(email);
//        UserDTO dto=new UserDTO();
//        dto.setId(user.getId());
//        dto.setRole(user.getRole());
//        dto.setDob(user.getDob());
//        dto.setEmail(user.getEmail());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setRegistered_date(user.getRegistered_date());
//        return dto;
//    }

    @Override
    public UserDTO getUser(String token) {
//    log.info("In getUser for token reset");

    // Validate the token and extract the email
    String email = jwtService.extractUserName(token);
//    log.info("Extracted email: {}", email);

    // Fetch the user by email
    User user = userRepository.findByEmail(email);
    if (email == null || user == null) {
//        log.error("Invalid token or user not found for email: {}", email);
        return null;
    }

    // Map User entity to UserDTO
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setRole(user.getRole());
    dto.setDob(user.getDob());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setRegistered_date(user.getRegistered_date());

    return dto;
}
}
