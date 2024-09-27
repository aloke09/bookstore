package com.bridgelabz.user_microservice1.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO
{
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registered_date;
    private String email;
    private String role;
}
