package com.example.manikantasales.service;

import com.example.manikantasales.dto.CartResponse;
import com.example.manikantasales.entity.Cart;

import java.util.List;

public interface CartService {

    Cart addToCart(Long productId, Integer quantity);

    List<CartResponse> getMyCart();

    Cart increaseQuantity(Long cartId);

    Cart decreaseQuantity(Long cartId);

    void removeCartItem(Long cartId);

    void clearCart();

    long getCartCount();
}