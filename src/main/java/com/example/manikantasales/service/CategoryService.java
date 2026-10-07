package com.example.manikantasales.service;


import com.example.manikantasales.dto.CategoryRequest;
import com.example.manikantasales.dto.CategoryResponse;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



public interface CategoryService {



    // =====================================
    // ADD CATEGORY
    // =====================================

    CategoryResponse addCategory(

            CategoryRequest request,

            MultipartFile image

    );





    // =====================================
    // GET ALL CATEGORIES
    // =====================================

    List<CategoryResponse> getAllCategories();





    // =====================================
    // PAGINATION
    // =====================================

    Page<CategoryResponse> getAllCategories(

            int page,

            int size

    );





    // =====================================
    // GET CATEGORY BY ID
    // =====================================

    CategoryResponse getCategoryById(

            Long id

    );





    // =====================================
    // UPDATE CATEGORY
    // =====================================

    CategoryResponse updateCategory(

            Long id,

            CategoryRequest request,

            MultipartFile image

    );





    // =====================================
    // DELETE CATEGORY
    // =====================================

    String deleteCategory(

            Long id

    );





    // =====================================
    // SEARCH CATEGORY
    // =====================================

    List<CategoryResponse> searchCategories(

            String keyword

    );

    Long getCategoryCount();
}