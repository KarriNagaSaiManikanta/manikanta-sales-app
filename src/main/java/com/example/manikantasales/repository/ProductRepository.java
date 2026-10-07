package com.example.manikantasales.repository;

import com.example.manikantasales.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {


    // =====================================
    // ACTIVE PRODUCTS PAGINATION
    // =====================================

    Page<Product> findByDeletedFalse(
            Pageable pageable
    );


    // =====================================
    // FIND ACTIVE PRODUCT BY ID
    // =====================================

    Optional<Product> findByIdAndDeletedFalse(
            Long id
    );


    // =====================================
    // SEARCH PRODUCTS
    // Search by:
    // Product Name
    // Brand
    // Description
    // Category Name
    // =====================================

    @Query("""
            SELECT p
            FROM Product p

            WHERE p.deleted = false

            AND
            (
                LOWER(p.name)
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(COALESCE(p.brand, ''))
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(COALESCE(p.description, ''))
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(p.category.name)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    Page<Product> searchProducts(

            @Param("keyword")
            String keyword,

            Pageable pageable

    );


    // =====================================
    // PRODUCTS BY CATEGORY
    // =====================================

    @Query("""
            SELECT p
            FROM Product p

            WHERE p.deleted = false

            AND p.category.id = :categoryId

            """)
    Page<Product> findByCategory(

            @Param("categoryId")
            Long categoryId,

            Pageable pageable

    );


    // =====================================
    // SEARCH + CATEGORY
    // =====================================

    @Query("""
            SELECT p
            FROM Product p

            WHERE p.deleted = false

            AND p.category.id = :categoryId

            AND
            (
                LOWER(p.name)
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(COALESCE(p.brand, ''))
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(COALESCE(p.description, ''))
                LIKE LOWER(CONCAT('%', :keyword, '%'))

                OR

                LOWER(p.category.name)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            )

            """)
    Page<Product> searchByKeywordCategory(

            @Param("keyword")
            String keyword,

            @Param("categoryId")
            Long categoryId,

            Pageable pageable

    );


    // =====================================
    // DUPLICATE PRODUCT CHECK
    // =====================================

    boolean existsByNameAndDeletedFalse(
            String name
    );


    // =====================================
    // TOTAL PRODUCTS
    // =====================================

    long countByDeletedFalse();

}