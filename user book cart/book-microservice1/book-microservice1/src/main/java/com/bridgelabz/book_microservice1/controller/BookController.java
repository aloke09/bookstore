package com.bridgelabz.book_microservice1.controller;

import com.bridgelabz.book_microservice1.client.UserServiceClient;
import com.bridgelabz.book_microservice1.external.User;
import com.bridgelabz.book_microservice1.model.Books;
import com.bridgelabz.book_microservice1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController
{
    @Autowired
    private BookService bookService;

    @Autowired
    private UserServiceClient userServiceClient;

    private User getAuthenticatedAdminUser(String authHeader) {
        User user = userServiceClient.getUser(authHeader);
        System.out.println(user);
        if(user!=null && user.getRole().equals("admin"))
        {
            return user;
        }
        return null;
    }


    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBookWithLogoWithToken(@RequestHeader("Authorization") String authHeader,
                                                      @RequestPart("books") Books books,
                                                      @RequestPart("booklogo") MultipartFile file)
    {
        try
        {
            User isAdmin=getAuthenticatedAdminUser(authHeader);
            if(isAdmin!=null)
            {
                Books books1 = bookService.addBook(books, file);
                return new ResponseEntity<>(books1, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e)
        {
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
    public ResponseEntity<String> deleteById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id)
    {
        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null)
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
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value="/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBookLogoById(@RequestHeader("Authorization") String authHeader, @RequestPart("booklogo") MultipartFile file,
                                                @PathVariable Long id) {
        try
        {
            User isAdmin=getAuthenticatedAdminUser(authHeader);
            if(isAdmin!=null )
            {
                System.out.println("Book ID ---> " + id);
                if (file.isEmpty()) {
                    return new ResponseEntity<>("File is empty, please upload a valid image.", HttpStatus.BAD_REQUEST);
                }

                Books books = bookService.updateBookLogoById(id, file);
                if (books != null) {
                    return new ResponseEntity<>(books, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Book ID not found.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO
    @PutMapping("/update/qty/{id}")
    public ResponseEntity<String> changeBookQty(@RequestHeader("Authorization") String authHeader, @PathVariable long id,@RequestPart("qty") long qty)
    {
        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null)
        {
            String s = bookService.ChangeBookQuantity(id, qty);
            if(s!=null)
            {
                return new ResponseEntity<>(s,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    // TODO
    @PutMapping("/update/price/{id}")
    public ResponseEntity<String> changeBookPrice(@RequestHeader("Authorization") String authHeader, @PathVariable long id,@RequestPart("price") long price)
    {

        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null )
        {
            String s = bookService.ChangeBookPrice(id, price);
            if(s!=null)
            {
                return new ResponseEntity<>(s,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    @PutMapping("books/minusAddToCartQuantity/{bookId}")
//    String minusAddToCartQuantity(@PathVariable Long bookId);

    @PutMapping("minusAddToCartQuantity/{bookId}")
    public String minusAddToCartQuantity(@PathVariable Long bookId)
    {
        return bookService.minusAddToCartQuantity(bookId);
    }

    @PutMapping("/removeFromCartAddToBook/{bookId}/{qty}")
    public ResponseEntity<String> removeFromCartAddToBook(@PathVariable Long bookId,@PathVariable Long qty) {
        String result = bookService.removeFromCartAddToBook(bookId,qty);
        if (result.equals("Quantity added back to book table")) {
            return ResponseEntity.ok(result);  // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);  // 404 Not Found
        }
    }



    @PutMapping("/canceled/{bookId}/{qty}")
    public ResponseEntity<String> orderedCanceledAddBackToBook(@PathVariable long bookId,
                                                               @PathVariable long qty,
                                                               @RequestHeader("Authorization") String authHeader)
    {
        User isAdmin=getAuthenticatedAdminUser(authHeader);
        if(isAdmin!=null)
        {
            String s = bookService.addBackToBookTable(bookId, qty);
            if(s!=null)
            {
                return new ResponseEntity<>(s,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("book id not found!!",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
