package com.bridgelabz.book_microservice1.external;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class User
{
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registered_date;
    private String email;
    private String role;

}
