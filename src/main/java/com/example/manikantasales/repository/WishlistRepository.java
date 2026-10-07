package com.example.manikantasales.repository;


import com.example.manikantasales.entity.Wishlist;
import com.example.manikantasales.entity.User;
import com.example.manikantasales.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WishlistRepository 
extends JpaRepository<Wishlist,Long>{


    List<Wishlist> findByUser(User user);


    boolean existsByUserAndProduct(
            User user,
            Product product
    );


    void deleteByUserAndProduct(
            User user,
            Product product
    );

}