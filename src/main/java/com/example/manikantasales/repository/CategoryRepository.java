package com.example.manikantasales.repository;


import com.example.manikantasales.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;



public interface CategoryRepository 
        extends JpaRepository<Category, Long> {



    // =====================================
    // DUPLICATE CATEGORY CHECK
    // =====================================

    @Query("""
            SELECT COUNT(c) > 0
            FROM Category c
            WHERE LOWER(TRIM(c.name)) = LOWER(TRIM(:name))
            AND c.deleted = false
            """)
    boolean existsByNameAndDeletedFalse(

            @Param("name") String name

    );




    // =====================================
    // GET ACTIVE CATEGORIES
    // =====================================

    List<Category> findByDeletedFalse();





    // =====================================
    // PAGINATION
    // =====================================

    Page<Category> findByDeletedFalse(

            Pageable pageable

    );





    // =====================================
    // FIND ACTIVE CATEGORY BY ID
    // =====================================

    Optional<Category> findByIdAndDeletedFalse(

            Long id

    );





    // =====================================
    // SEARCH CATEGORY
    // =====================================

    @Query("""
            SELECT c
            FROM Category c
            WHERE c.deleted = false
            AND LOWER(c.name)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Category> searchCategory(

            @Param("keyword") String keyword

    );

    long countByDeletedFalse();
}