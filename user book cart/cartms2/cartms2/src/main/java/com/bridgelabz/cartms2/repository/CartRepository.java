package com.bridgelabz.cartms2.repository;

import com.bridgelabz.cartms2.model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartModel,Long>
{

    List<CartModel> findAllByUserId(Long id);
    CartModel findByUserId(Long id);
    void deleteAllByUserId(Long id);
}
