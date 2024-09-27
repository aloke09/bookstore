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
    
    private String addToCartQty(Long bookId)//minus from book table
    {
        System.out.println("book id --->"+bookId);
        String s = bookServiceClient.minusAddToCartQuantity(bookId);
        return s;
    }
    private String removeFromCartAddToBook(Long BookId)
    {
        String s = bookServiceClient.removeFromCartAddToBook(BookId);
        return s;
    }


    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> add(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable Long bookId)
    {
        User isValidUser=getAuthenticatedUser(authHeader);
        System.out.println(isValidUser.toString());//print user details

        if(isValidUser!=null)
        {
            Long userId=isValidUser.getId();
            System.out.println("user id---->"+userId);
            Book bookId1 = getBookId(bookId);
            String added = cartService.add(userId, bookId1);


            String s = addToCartQty(bookId1.getId());
            System.out.println(s);
            return new ResponseEntity<>(added,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/addtocart")
//    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader,
//                                       @RequestBody CartModel cartModel)
//    {
//        User isValidUser=getAuthenticatedUser(authHeader);
//        if(isValidUser!=null)
//        {
//            Book isValidBook=getBookId(cartModel.getBookId());
//            if(isValidBook!=null)
//            {
//                if(isValidBook.getBookQuantity()>=cartModel.getQuantity())
//                {
//                    cartModel.setTotalPrice(cartModel.getQuantity()*isValidBook.getBookPrice());
//
//                    CartModel cartModel1 = cartService.addToCart(cartModel);
//
//                    System.out.println("added to cart ===>"+cartModel1);
//
//                    String s = addToCartQty(cartModel.getBookId(), cartModel.getQuantity());//minus from book table
//
//                    return new ResponseEntity<>(s, HttpStatus.OK);
//                }
//                else
//                {
//                    return new ResponseEntity<>("Out of stock",HttpStatus.INSUFFICIENT_STORAGE);
//                }
//            }
//            else
//            {
//                return new ResponseEntity<>("invalid book",HttpStatus.UNAUTHORIZED);
//            }
//        }
//        else
//        {
//            return new ResponseEntity<>("invalid credentials",HttpStatus.UNAUTHORIZED);
//        }
//    }

    @DeleteMapping("/removefromcart/{cartId}")
    public ResponseEntity<?> removeFromCart(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long cartId)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser != null)
        {
            CartModel cartExists = cartService.getById(cartId);
            if(cartExists!=null)
            {
                Long bookId = cartExists.getBookId();

                Book book = getBookId(bookId);//it should exists in book table
                System.out.println(bookId+"book--->"+book.getId());
                if(book!=null)
                {
                    System.out.println(bookId);
                    String s = removeFromCartAddToBook(bookId);
                    System.out.println("msg after book added to book table-->"+s);
                    String byId1 = cartService.deleteCartByUserIdAndCartId(isValidUser.getId(), cartId);
                    return  new ResponseEntity<>(byId1,HttpStatus.OK);

                }
                else
                {
                    return new ResponseEntity<>("book not found",HttpStatus.NOT_FOUND);
                }
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/allCartItems")//admin
    public ResponseEntity<List<CartModel>> getAllCartItems(@RequestHeader("Authorization") String authHeader)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser!=null && isValidUser.getRole().equals("admin"))
        {
            List<CartModel> all = cartService.getAll();
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        else
        {
            System.out.println("admin access required");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/CartItemsForUser/userId")//normal user
    public List<CartModel> getAllCartItemsForA_User(@RequestHeader("Authorization") String authHeader)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if (isValidUser!=null)
        {
            long userId = isValidUser.getId();
            System.out.println("firstname---------->"+isValidUser.getFirstName());
            List<CartModel> listOfAllCartDetailsForUser=cartService.getByAllUserId(userId);
            return listOfAllCartDetailsForUser;
//            return new ResponseEntity<>(cartService.getByUserId(isValidUser.getId()),HttpStatus.OK);
        }
        else
        {
//            System.out.println("admin access required");
            return null;
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

    @GetMapping("/removeAllCartItemsForUser/userId")//normal user
    public ResponseEntity<String> deleteAllByUserId(@RequestHeader("Authorization") String authHeader)
    {
        User isValidUser = getAuthenticatedUser(authHeader);
        if(isValidUser!=null)
        {
            cartService.removeFromCartForUser(isValidUser.getId());
            return new ResponseEntity<>("removed user id",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("invalid token/id",HttpStatus.UNAUTHORIZED);
        }
    }

}
