package com.example.manikantasales.repository;


import com.example.manikantasales.entity.Cart;
import com.example.manikantasales.entity.Product;
import com.example.manikantasales.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;



public interface CartRepository 
        extends JpaRepository<Cart, Long> {



    // =====================================
    // GET CART WITH PRODUCT DETAILS
    // =====================================

    @Query("""
            SELECT c
            FROM Cart c
            JOIN FETCH c.product p
            LEFT JOIN FETCH p.category
            WHERE c.user = :user
            """)
    List<Cart> findCartWithProduct(
            @Param("user") User user
    );





    // =====================================
    // FIND EXISTING CART ITEM
    // =====================================

    Optional<Cart> findByUserAndProduct(
            User user,
            Product product
    );






    // =====================================
    // TOTAL CART QUANTITY
    // Navbar Cart Badge
    // =====================================

    @Query("""
            SELECT COALESCE(SUM(c.quantity),0)
            FROM Cart c
            WHERE c.user = :user
            """)
    long getTotalQuantityByUser(
            @Param("user") User user
    );






    // =====================================
    // CLEAR CART
    // =====================================

    void deleteByUser(
            User user
    );



}