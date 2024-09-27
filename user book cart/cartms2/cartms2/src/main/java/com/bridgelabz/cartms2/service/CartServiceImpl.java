package com.bridgelabz.cartms2.service;

import com.bridgelabz.cartms2.external.Book;
import com.bridgelabz.cartms2.external.User;
import com.bridgelabz.cartms2.model.CartModel;
import com.bridgelabz.cartms2.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl
{
    @Autowired
    private CartRepository cartRepository;

    public CartModel addToCart(CartModel cartModel)
    {
//        cartRepository.save(cartModel);
//        cartModel.setTotalPrice(cartModel.getQuantity()*price);//total price//handled in controller

        return cartRepository.save(cartModel);
    }

    public CartModel getById(Long id)
    {
        CartModel exists = cartRepository.findById(id).orElse(null);
        if(exists!=null)
        {
            return exists;
        }
        else
        {
            return null;
        }
    }

    public String removeFromCart(long id)
    {
        cartRepository.deleteById(id);
        return "removed from cart successfully!!";
    }

    public List<CartModel> getAll()
    {
        return cartRepository.findAll();
    }

    public List<CartModel> getByAllUserId(Long userId)
    {
        List<CartModel> all = cartRepository.findAllByUserId(userId);
        List<CartModel> byUserId=new ArrayList<>();
        for (CartModel c:all)
        {
            if(Objects.equals(c.getUserId(), userId))
            {
                byUserId.add(c);
                System.out.println(c);
            }
        }

        return byUserId;//cartRepository.findByUserId(id);
    }
    public CartModel updateQty(Long id,Long qty)
    {
        CartModel cartModel = cartRepository.findById(id).orElse(null);
        if(cartModel!=null && qty>0)
        {
            cartModel.setQuantity(qty);
//            long updatedQty=cartModel.getQuantity();
//
//            cartModel.setTotalPrice(cartModel.getTotalPrice()*cartModel.getQuantity());
            return cartRepository.save(cartModel);
        }
        else {
            return  null;
        }
    }

    //remove from cart where ui=?
    public String removeFromCartForUser(Long userId)
    {
        cartRepository.deleteAllByUserId(userId);
        return "deleted successfully!!";
    }

    public String add(Long userId,Book bookId1)
    {
        CartModel cm=new CartModel();
        cm.setQuantity(1L);
        cm.setTotalPrice(bookId1.getBookPrice());
        cm.setUserId(userId);
        cm.setBookId(bookId1.getId());
        cartRepository.save(cm);
        return "book details added to cart!!";
    }

    public String deleteCartByUserIdAndCartId(long userId, Long cartId)
    {
        List<CartModel> allByUserId = cartRepository.findAllByUserId(userId);
        for(CartModel c: allByUserId)
        {
            if(c.getCartId()==cartId)
            {
                cartRepository.deleteById(cartId);
                return "cart deleted for user id "+userId+" with cart id "+cartId;

            }
        }
        return "the user does not have this cart access";
    }
}
