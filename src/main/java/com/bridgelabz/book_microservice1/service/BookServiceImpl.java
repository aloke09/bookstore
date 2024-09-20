package com.bridgelabz.book_microservice1.service;

import com.bridgelabz.book_microservice1.model.Books;
import com.bridgelabz.book_microservice1.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService
{
    @Autowired
    private BookRepository bookRepository;


    @Override
    public Books addBook(Books book, MultipartFile file) throws IOException {
        book.setBookLogo(file.getBytes());
        return bookRepository.save(book);
    }

    @Override
    public List<Books> getAllBookDetails() {
        return bookRepository.findAll();
    }

    @Override
    public Books getBookDetailsById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteBookDetailsById(Long id)
    {
        Books founded = bookRepository.findById(id).orElse(null);
        if(founded!=null)
        {
            bookRepository.deleteById(id);
            return "delete successfully!!";
        }
        else
        {
            return "book id not found!!";
        }

    }

    @Override
    public Books updateBookLogoById(Long id, MultipartFile file) throws IOException
    {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookLogo(file.getBytes());
        return bookRepository.save(books);
    }

    @Override
    public Books ChangeBookQuantity(Long id, int quantity)
    {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookQuantity(quantity);
        return bookRepository.save(books);
    }

    @Override
    public Books ChangeBookPrice(Long id, double price)
    {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookPrice(price);
        return bookRepository.save(books);
    }


}
