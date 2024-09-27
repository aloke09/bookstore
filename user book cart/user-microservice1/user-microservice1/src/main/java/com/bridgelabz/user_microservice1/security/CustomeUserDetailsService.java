package com.bridgelabz.user_microservice1.security;

import com.bridgelabz.user_microservice1.model.User;
import com.bridgelabz.user_microservice1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomeUserDetailsService implements UserDetailsService
{
    private UserRepository userRepository;

    public CustomeUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User userByEmail = userRepository.findByEmail(email);
        System.out.println("user details ----->"+userByEmail);

        if(userByEmail==null)
        {
            System.out.println("no student available");
            throw new UsernameNotFoundException("EMAIL NOT FOUND!!");
        }

        return new CustomUserdetails(userByEmail);
    }
}
