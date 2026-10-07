package com.example.manikantasales.controller;


import com.example.manikantasales.dto.CategoryRequest;
import com.example.manikantasales.dto.CategoryResponse;
import com.example.manikantasales.service.CategoryService;


import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;



@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {


    private final CategoryService categoryService;



    // =====================================
    // CONSTRUCTOR
    // =====================================

    public CategoryController(
            CategoryService categoryService
    ){

        this.categoryService = categoryService;

    }







    // =====================================
    // ADD CATEGORY
    // =====================================


    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CategoryResponse> addCategory(


            @Valid
            @ModelAttribute CategoryRequest request,


            @RequestParam(
                    value="image",
                    required=false
            )
            MultipartFile image


    ){


        CategoryResponse response =

                categoryService.addCategory(
                        request,
                        image
                );



        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);


    }









    // =====================================
    // GET ALL WITH PAGINATION
    // =====================================


    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(


            @RequestParam(defaultValue="0")
            int page,


            @RequestParam(defaultValue="10")
            int size


    ){


        return ResponseEntity.ok(

                categoryService.getAllCategories(
                        page,
                        size
                )

        );


    }









    // =====================================
    // SEARCH
    // =====================================


    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(


            @RequestParam String keyword


    ){


        return ResponseEntity.ok(

                categoryService.searchCategories(keyword)

        );


    }









    // =====================================
    // GET BY ID
    // =====================================


    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(


            @PathVariable Long id


    ){


        return ResponseEntity.ok(

                categoryService.getCategoryById(id)

        );


    }









 // =====================================
 // UPDATE CATEGORY
 // =====================================

 @PutMapping(
         value = "/{id}",
         consumes = MediaType.MULTIPART_FORM_DATA_VALUE
 )
 public ResponseEntity<CategoryResponse> updateCategory(

         @PathVariable Long id,

         @Valid
         @ModelAttribute CategoryRequest request,

         @RequestParam(
                 value = "image",
                 required = false
         )
         MultipartFile image

 ) {

     System.out.println("======================================");
     System.out.println("UPDATE METHOD CALLED");
     System.out.println("ID = " + id);
     System.out.println("NAME = " + request.getName());
     System.out.println("DESCRIPTION = " + request.getDescription());
     System.out.println("ACTIVE = " + request.isActive());
     System.out.println("IMAGE = " + (image == null ? "NULL" : image.getOriginalFilename()));
     System.out.println("======================================");

     CategoryResponse response =
             categoryService.updateCategory(id, request, image);

     return ResponseEntity.ok(response);
 }

    // =====================================
    // DELETE
    // =====================================


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(


            @PathVariable Long id


    ){


        return ResponseEntity.ok(

                categoryService.deleteCategory(id)

        );


    }
 // =====================================
 // TOTAL CATEGORY COUNT
 // =====================================
 @GetMapping("/count")
 public ResponseEntity<Long> getCategoryCount() {

     return ResponseEntity.ok(
             categoryService.getCategoryCount()
     );

 }

}