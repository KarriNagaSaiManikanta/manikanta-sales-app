package com.example.manikantasales.controller;

import com.example.manikantasales.dto.CartResponse;
import com.example.manikantasales.entity.Cart;
import com.example.manikantasales.service.CartService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // =====================================
    // ADD CART
    // =====================================

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {

        cartService.addToCart(productId, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product added to cart");

        return ResponseEntity.ok(response);
    }

    // =====================================
    // GET USER CART
    // =====================================

    @GetMapping
    public ResponseEntity<List<CartResponse>> getCart() {

        return ResponseEntity.ok(
                cartService.getMyCart()
        );
    }

    // =====================================
    // INCREASE QUANTITY
    // =====================================

    @PutMapping("/increase/{cartId}")
    public ResponseEntity<Cart> increase(
            @PathVariable Long cartId
    ) {

        return ResponseEntity.ok(
                cartService.increaseQuantity(cartId)
        );
    }

    // =====================================
    // DECREASE QUANTITY
    // =====================================

    @PutMapping("/decrease/{cartId}")
    public ResponseEntity<?> decrease(
            @PathVariable Long cartId
    ) {

        Cart cart = cartService.decreaseQuantity(cartId);

        if (cart == null) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cart item removed");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(cart);
    }

    // =====================================
    // REMOVE ITEM
    // =====================================

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Map<String, Object>> remove(
            @PathVariable Long cartId
    ) {

        cartService.removeCartItem(cartId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item removed successfully");

        return ResponseEntity.ok(response);
    }

    // =====================================
    // CLEAR CART
    // =====================================

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clear() {

        cartService.clearCart();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cart cleared successfully");

        return ResponseEntity.ok(response);
    }

    // =====================================
    // CART COUNT
    // =====================================

    @GetMapping("/count")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(
                cartService.getCartCount()
        );
    }

}