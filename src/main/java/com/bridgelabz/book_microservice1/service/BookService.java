package com.bridgelabz.book_microservice1.service;

import com.bridgelabz.book_microservice1.model.Books;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService
{
    Books addBook(Books book, MultipartFile file) throws IOException;
    List<Books> getAllBookDetails();
    Books getBookDetailsById(Long id);
    String deleteBookDetailsById(Long id);
    Books updateBookLogoById(Long id,MultipartFile file) throws IOException;
    Books ChangeBookQuantity(Long bookid,int quantity);
    Books ChangeBookPrice(Long bookid,double price);
}
