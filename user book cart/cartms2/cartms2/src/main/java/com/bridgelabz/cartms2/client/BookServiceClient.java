package com.bridgelabz.cartms2.client;

import com.bridgelabz.cartms2.external.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "BOOK-MICROSERVICE1")
public interface BookServiceClient {

    @GetMapping("books/{id}")
    Book aParticularBook(@PathVariable Long id);
//    Quantity

    @PutMapping("books/minusAddToCartQuantity/{bookId}")
    String minusAddToCartQuantity(@PathVariable Long bookId);

//    @DeleteMapping("cart/removeFromCart/{bookId}")
    @PutMapping("books/removeFromCartAddToBook/{bookId}")
    String removeFromCartAddToBook(@PathVariable Long bookId);

}