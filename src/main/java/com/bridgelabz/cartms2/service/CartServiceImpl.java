package com.bridgelabz.cartms2.service;

import com.bridgelabz.cartms2.model.CartModel;
import com.bridgelabz.cartms2.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            cartRepository.deleteById(id);
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

    public CartModel getByUserId(Long id)
    {
        return cartRepository.findByUserId(id);
    }
    public CartModel updateQty(Long id,Long qty)
    {
        CartModel cartModel = cartRepository.findById(id).orElse(null);
        if(cartModel!=null && qty>0)
        {
            cartModel.setQuantity(qty);
            return cartRepository.save(cartModel);
        }
        else {
            return  null;
        }

    }

}
