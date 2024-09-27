package com.bridgelabz.cartms2.external;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book
{
    private long id;

    private String bookName;
    private String bookAuthor;
    private String bookDescription;

    @Lob
    private byte[] bookLogo;
    private long bookPrice;
    private long bookQuantity;

}
