package com.example.manikantasales.controller;

import com.example.manikantasales.entity.Product;
import com.example.manikantasales.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // =====================================
    // ADD PRODUCT
    // =====================================

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(

            @RequestParam String name,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            String brand,

            @RequestParam String price,

            @RequestParam Integer quantity,

            @RequestParam Long categoryId,

            @RequestParam(value = "image", required = false)
            MultipartFile image

    ) {

        System.out.println("========== ADD PRODUCT ==========");

        System.out.println("Name      : " + name);
        System.out.println("Brand     : " + brand);
        System.out.println("Price     : " + price);
        System.out.println("Quantity  : " + quantity);
        System.out.println("Category  : " + categoryId);
        System.out.println(
                "Image     : " +
                (image != null
                        ? image.getOriginalFilename()
                        : "No Image")
        );

        System.out.println("=================================");

        return ResponseEntity.ok(

                productService.addProduct(
                        name,
                        description,
                        brand,
                        price,
                        quantity,
                        categoryId,
                        image
                )

        );
    }


    // =====================================
    // GET ALL PRODUCTS
    // =====================================

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String sort

    ) {

        Sort sorting = Sort.unsorted();

        if ("asc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").ascending();

        } else if ("desc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").descending();

        }

        Pageable pageable =
                PageRequest.of(page, size, sorting);

        return ResponseEntity.ok(

                productService.getAllProducts(pageable)

        );
    }


    // =====================================
    // SEARCH PRODUCTS
    // =====================================

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String sort

    ) {

        Sort sorting = Sort.unsorted();

        if ("asc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").ascending();

        } else if ("desc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").descending();

        }

        Pageable pageable =
                PageRequest.of(page, size, sorting);

        return ResponseEntity.ok(

                productService.searchProducts(
                        keyword,
                        pageable
                )

        );
    }


    // =====================================
    // PRODUCTS BY CATEGORY
    // =====================================

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> productsByCategory(

            @PathVariable Long categoryId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String sort

    ) {

        Sort sorting = Sort.unsorted();

        if ("asc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").ascending();

        } else if ("desc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").descending();

        }

        Pageable pageable =
                PageRequest.of(page, size, sorting);

        return ResponseEntity.ok(

                productService.getProductsByCategory(
                        categoryId,
                        pageable
                )

        );
    }


    // =====================================
    // GET PRODUCT BY ID
    // =====================================

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(

            @PathVariable Long id

    ) {

        return ResponseEntity.ok(

                productService.getProductById(id)

        );
    }


    // =====================================
    // UPDATE PRODUCT
    // =====================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Product> updateProduct(

            @PathVariable Long id,

            @RequestParam String name,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            String brand,

            @RequestParam String price,

            @RequestParam Integer quantity,

            @RequestParam Long categoryId,

            @RequestParam(value = "image", required = false)
            MultipartFile image

    ) {

        System.out.println("======= UPDATE PRODUCT =======");

        System.out.println("ID        : " + id);
        System.out.println("Name      : " + name);
        System.out.println("Brand     : " + brand);
        System.out.println("Price     : " + price);
        System.out.println("Quantity  : " + quantity);

        System.out.println("==============================");

        return ResponseEntity.ok(

                productService.updateProduct(
                        id,
                        name,
                        description,
                        brand,
                        price,
                        quantity,
                        categoryId,
                        image
                )

        );
    }


    // =====================================
    // DELETE PRODUCT
    // =====================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(

            @PathVariable Long id

    ) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }


    // =====================================
    // TOTAL PRODUCTS COUNT
    // =====================================

    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount() {

        return ResponseEntity.ok(

                productService.getProductCount()

        );
    }


    // =====================================
    // SEARCH + CATEGORY + PRICE SORTING
    // =====================================

    @GetMapping("/filter")
    public ResponseEntity<Page<Product>> filterProducts(
            @RequestParam String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String sort
    ) {

        Sort sorting = Sort.unsorted();

        if ("asc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").ascending();

        } else if ("desc".equalsIgnoreCase(sort)) {

            sorting = Sort.by("price").descending();

        }

        Pageable pageable =
                PageRequest.of(page, size, sorting);


        // =====================================
        // SEARCH + CATEGORY
        // =====================================

        if (categoryId != null) {

            return ResponseEntity.ok(
                    productService.searchByKeywordCategory(
                            keyword,
                            categoryId,
                            pageable
                    )
            );

        }


        // =====================================
        // SEARCH ONLY
        // =====================================

        return ResponseEntity.ok(
                productService.searchProducts(
                        keyword,
                        pageable
                )
        );
    }
}