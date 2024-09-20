package com.bridgelabz.book_microservice1.controller;

import com.bridgelabz.book_microservice1.model.Books;
import com.bridgelabz.book_microservice1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController
{
    @Autowired
    private BookService bookService;

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBookWithLogo(@RequestPart("books") Books books,
                                             @RequestPart("booklogo") MultipartFile file)
    {
        try
        {
            System.out.println(books);
            Books books1 = bookService.addBook(books, file);
            return new ResponseEntity<>(books1, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/all")
    public List<Books> getAllBooks()
    {
        return bookService.getAllBookDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> aParticularBook(@PathVariable Long id)
    {
        Books bookDetailsById = bookService.getBookDetailsById(id);
        if(bookDetailsById!=null)
        {
            return new ResponseEntity<>(bookDetailsById,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("book id not found !!",HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id)
    {
        String s = bookService.deleteBookDetailsById(id);
        if(s.equals("delete successfully!!"))
        {
            return new ResponseEntity<>(s,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("book id not found",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBookLogoById(@RequestPart("booklogo") MultipartFile file,
                                                @PathVariable Long id) {
        try {
            System.out.println("Book ID ---> " + id);

            // Check if the file is not empty
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty, please upload a valid image.", HttpStatus.BAD_REQUEST);
            }

            Books books = bookService.updateBookLogoById(id, file);
            if (books != null) {
                return new ResponseEntity<>(books, HttpStatus.OK); // 200 OK, update successful
            } else {
                return new ResponseEntity<>("Book ID not found.", HttpStatus.NOT_FOUND); // 404 Not Found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @PutMapping("/update/qty/{id}")
    public ResponseEntity<?> changeBookQty(@PathVariable long id,@RequestPart("qty") int qty)
    {
        Books books = bookService.ChangeBookQuantity(id, qty);
        if(books!=null)
        {
            return new ResponseEntity<>(books,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/price/{id}")
    public ResponseEntity<?> changeBookPrice(@PathVariable long id,@RequestPart("price") double price)
    {
        Books books = bookService.ChangeBookPrice(id, price);
        if(books!=null)
        {
            return new ResponseEntity<>(books,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
        }
    }

}
