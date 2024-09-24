package com.bridgelabz.cartms2.external;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class User
{
    private long id;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registered_date;
    private LocalDate updated_date;
    private String email;
    private String password;
    private String role;
}
