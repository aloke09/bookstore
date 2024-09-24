package com.bridgelabz.cartms2.Controller;

import com.bridgelabz.cartms2.client.BookServiceClient;
import com.bridgelabz.cartms2.client.UserServiceClient;
import com.bridgelabz.cartms2.external.Book;
import com.bridgelabz.cartms2.external.User;
import com.bridgelabz.cartms2.model.CartModel;
import com.bridgelabz.cartms2.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController
{
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private BookServiceClient bookServiceClient;

    private User getAuthenticatedUser(String authHeader) {
        User user = userServiceClient.getUser(authHeader);
        System.out.println(user);
        if(user!=null)
        {
            return user;
        }
        return null;
    }
    private Book getBookId(Long bookId)
    {
        Book book = bookServiceClient.aParticularBook(bookId);
        return  book;
    }
    
    private String addToCartQty(Long id,Long qty)//minus from book table
    {
        String s = bookServiceClient.minusAddToCartQuantity(id, qty);
        return s;
    }
    private String removeFromCartAddToBook(Long id,Long qty)
    {
        String s = bookServiceClient.removeFromCartAddToBook(id, qty);
        return s;
    }

    @PostMapping("/addtocart")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody CartModel cartModel)
    {
        User isValidUser=getAuthenticatedUser(authHeader);
        if(isValidUser!=null)
        {
            Book isValidBook=getBookId(cartModel.getBookId());
            if(isValidBook!=null)
            {
                if(isValidBook.getBookQuantity()>=cartModel.getQuantity())
                {
                    cartModel.setTotalPrice(cartModel.getQuantity()*isValidBook.getBookPrice());

                    CartModel cartModel1 = cartService.addToCart(cartModel);

                    System.out.println("added to cart ===>"+cartModel1);

                    String s = addToCartQty(cartModel.getBookId(), cartModel.getQuantity());//minus from book table

                    return new ResponseEntity<>(s, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>("Out of stock",HttpStatus.INSUFFICIENT_STORAGE);
                }
            }
            else
            {
                return new ResponseEntity<>("invalid book",HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>("invalid credentials",HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/removefromcart/{cartId}")
    public ResponseEntity<?> removeFromCart(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long cartId)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser != null)
        {
            CartModel byId = cartService.getById(cartId);
            Long bookId = byId.getBookId();
            Long quantity = byId.getQuantity();

            if (byId!=null)
            {
                String s = cartService.removeFromCart(byId.getCartId());
                System.out.println(s);
                String s1 = removeFromCartAddToBook(bookId, quantity);
                System.out.println(s1);
                return new ResponseEntity<>(s,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("user does not have any thing in his/her cart",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/allCartItems")//admin
    public List<CartModel> getAllCartItems(@RequestHeader("Authorization") String authHeader)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser!=null && isValidUser.getRole().equals("admin"))
        {
            return cartService.getAll();
        }
        else
        {
            System.out.println("admin access required");
            return null;
        }
    }

    @GetMapping("/CartItemsForUser")//normal user
    public ResponseEntity<?> getAllCartItemsForA_User(@RequestHeader("Authorization") String authHeader)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser!=null)
        {
            return new ResponseEntity<>(cartService.getByUserId(isValidUser.getId()),HttpStatus.OK);
        }
        else
        {
            System.out.println("admin access required");
            return new ResponseEntity<>("invalid user",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateqty/{cartId}/{qty}")
    public ResponseEntity<?> updateCartQty(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long cartId,@PathVariable Long qty)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser!=null)
        {

            CartModel cartModel = cartService.updateQty(cartId, qty);
            if(cartModel!=null)
            {
                return new ResponseEntity<>(cartModel,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("invalid cart id",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
        }

    }

}
