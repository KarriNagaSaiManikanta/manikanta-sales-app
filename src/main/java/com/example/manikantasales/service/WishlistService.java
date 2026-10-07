package com.example.manikantasales.service;


import com.example.manikantasales.entity.Wishlist;

import java.util.List;


public interface WishlistService {


    Wishlist addWishlist(Long productId);


    List<Wishlist> getMyWishlist();


    void removeWishlist(Long productId);

}