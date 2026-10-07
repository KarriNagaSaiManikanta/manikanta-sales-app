package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.dto.CategoryRequest;
import com.example.manikantasales.dto.CategoryResponse;

import com.example.manikantasales.entity.Category;

import com.example.manikantasales.exception.ResourceNotFoundException;

import com.example.manikantasales.repository.CategoryRepository;

import com.example.manikantasales.service.CategoryService;
import com.example.manikantasales.service.FileUploadService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.stream.Collectors;



@Service
public class CategoryServiceImpl implements CategoryService {



    private final CategoryRepository categoryRepository;


    private final FileUploadService fileUploadService;



 // =====================================
 // FORMAT CATEGORY NAME
 // =====================================
 private String formatCategoryName(String name) {

     if (name == null || name.trim().isEmpty()) {
         return name;
     }

     name = name.trim().toLowerCase();

     return name.substring(0, 1).toUpperCase()
             + name.substring(1);
 }

    // =====================================
    // CONSTRUCTOR
    // =====================================


    public CategoryServiceImpl(

            CategoryRepository categoryRepository,

            FileUploadService fileUploadService

    ){

        this.categoryRepository = categoryRepository;

        this.fileUploadService = fileUploadService;

    }









    // =====================================
    // ADD CATEGORY
    // =====================================


    @Override
    public CategoryResponse addCategory(

            CategoryRequest request,

            MultipartFile image

    ){


        Category category = new Category();



        category.setName(
                formatCategoryName(request.getName())
        );



        category.setDescription(

                request.getDescription()

        );



        category.setActive(

                request.isActive()

        );


        category.setDeleted(false);




        // IMAGE UPLOAD

        if(image != null && !image.isEmpty()){


            String imagePath =

                    fileUploadService
                            .uploadCategoryImage(image);



            category.setImage(imagePath);


        }





        Category saved =

                categoryRepository.save(category);




        return mapToResponse(saved);


    }









    // =====================================
    // GET ALL
    // =====================================


    @Override
    public List<CategoryResponse> getAllCategories(){


        return categoryRepository

                .findByDeletedFalse()

                .stream()

                .map(this::mapToResponse)

                .collect(Collectors.toList());


    }









    // =====================================
    // PAGINATION
    // =====================================


    @Override
    public Page<CategoryResponse> getAllCategories(

            int page,

            int size

    ){


        return categoryRepository

                .findByDeletedFalse(

                        PageRequest.of(page,size)

                )

                .map(this::mapToResponse);


    }









    // =====================================
    // GET BY ID
    // =====================================


    @Override
    public CategoryResponse getCategoryById(Long id){


        Category category =

                categoryRepository

                .findByIdAndDeletedFalse(id)

                .orElseThrow(

                        () -> new ResourceNotFoundException(

                                "Category not found : " + id

                        )

                );



        return mapToResponse(category);


    }









    // =====================================
    // UPDATE CATEGORY
    // =====================================


    @Override
    public CategoryResponse updateCategory(

            Long id,

            CategoryRequest request,

            MultipartFile image

    ){



        Category category =

                categoryRepository

                .findByIdAndDeletedFalse(id)

                .orElseThrow(

                        () -> new ResourceNotFoundException(

                                "Category not found : " + id

                        )

                );




        category.setName(
                formatCategoryName(request.getName())
        );



        category.setDescription(

                request.getDescription()

        );



        category.setActive(

                request.isActive()

        );





        // UPDATE IMAGE

        if(image != null && !image.isEmpty()){


            String imagePath =

                    fileUploadService

                    .uploadCategoryImage(image);



            category.setImage(imagePath);


        }






        Category updated =

                categoryRepository.save(category);




        return mapToResponse(updated);


    }









    // =====================================
    // DELETE CATEGORY
    // SOFT DELETE
    // =====================================


    @Override
    public String deleteCategory(Long id){


        Category category =

                categoryRepository

                .findByIdAndDeletedFalse(id)

                .orElseThrow(

                        () -> new ResourceNotFoundException(

                                "Category not found : " + id

                        )

                );




        category.setDeleted(true);


        category.setActive(false);



        categoryRepository.save(category);




        return "Category Deleted Successfully";


    }









    // =====================================
    // SEARCH
    // =====================================


    @Override
    public List<CategoryResponse> searchCategories(

            String keyword

    ){


        return categoryRepository

                .searchCategory(keyword)

                .stream()

                .map(this::mapToResponse)

                .collect(Collectors.toList());


    }









    // =====================================
    // DTO MAPPER
    // =====================================


    private CategoryResponse mapToResponse(

            Category category

    ){



        return new CategoryResponse(

                category.getId(),

                category.getName(),

                category.getDescription(),

                category.getImage(),

                category.isActive()

        );


    }

    @Override
    public Long getCategoryCount() {

        return categoryRepository.countByDeletedFalse();

    }

}