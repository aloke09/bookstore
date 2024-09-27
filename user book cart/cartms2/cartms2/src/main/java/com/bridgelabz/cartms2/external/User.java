package com.bridgelabz.cartms2.external;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class User
{
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registered_date;
    private LocalDate updated_date;
    private String email;
    private String role;
}
