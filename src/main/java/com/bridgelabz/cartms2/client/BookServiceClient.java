package com.bridgelabz.cartms2.client;

import com.bridgelabz.cartms2.external.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "BOOK-MICROSERVICE1")
public interface BookServiceClient {

    @GetMapping("books/{id}")
    Book aParticularBook(@PathVariable Long id);
//    Quantity

    @PutMapping("books/minusAddToCartQunatity/{id}")
    String minusAddToCartQuantity(@PathVariable Long id, @RequestPart Long qty);

    @PutMapping("/removeFromCart/{id}")
    String removeFromCartAddToBook(@PathVariable Long id, @RequestPart Long qty);

}