package com.example.manikantasales.service;

import com.example.manikantasales.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // =====================================
    // ADD PRODUCT
    // =====================================

    Product addProduct(
            String name,
            String description,
            String brand,
            String price,
            Integer quantity,
            Long categoryId,
            MultipartFile image
    );


    // =====================================
    // GET ALL PRODUCTS
    // =====================================

    List<Product> getAllProducts();

    Page<Product> getAllProducts(
            int page,
            int size
    );

    Page<Product> getAllProducts(
            Pageable pageable
    );


    // =====================================
    // GET PRODUCT BY ID
    // =====================================

    Product getProductById(
            Long id
    );


    // =====================================
    // UPDATE PRODUCT
    // =====================================

    Product updateProduct(
            Long id,
            String name,
            String description,
            String brand,
            String price,
            Integer quantity,
            Long categoryId,
            MultipartFile image
    );


    // =====================================
    // DELETE PRODUCT
    // =====================================

    void deleteProduct(
            Long id
    );


    // =====================================
    // SEARCH PRODUCTS
    // =====================================

    Page<Product> searchProducts(
            String keyword,
            int page,
            int size
    );

    Page<Product> searchProducts(
            String keyword,
            Pageable pageable
    );


    // =====================================
    // CATEGORY PRODUCTS
    // =====================================

    Page<Product> getProductsByCategory(
            Long categoryId,
            int page,
            int size
    );

    Page<Product> getProductsByCategory(
            Long categoryId,
            Pageable pageable
    );


    // =====================================
    // SEARCH + CATEGORY
    // =====================================

    Page<Product> searchByKeywordCategory(
            String keyword,
            Long categoryId,
            Pageable pageable
    );


    // =====================================
    // TOTAL PRODUCTS
    // =====================================

    long getTotalProducts();

    long getProductCount();

}