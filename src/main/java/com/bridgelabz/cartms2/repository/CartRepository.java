package com.bridgelabz.cartms2.repository;

import com.bridgelabz.cartms2.model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartModel,Long>
{
    CartModel findByUserId(long id);
}
