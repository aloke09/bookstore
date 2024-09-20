package com.bridgelabz.book_microservice1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private String type;
    @Lob
    private byte[] bookLogo;
    private double bookPrice;
    private int bookQuantity;


}
