package com.example.manikantasales.serviceimpl;

import com.example.manikantasales.entity.Category;
import com.example.manikantasales.entity.Product;
import com.example.manikantasales.exception.ResourceNotFoundException;
import com.example.manikantasales.repository.CategoryRepository;
import com.example.manikantasales.repository.ProductRepository;
import com.example.manikantasales.service.FileUploadService;
import com.example.manikantasales.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final FileUploadService fileUploadService;


    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProductServiceImpl(

            ProductRepository productRepository,

            CategoryRepository categoryRepository,

            FileUploadService fileUploadService

    ) {

        this.productRepository = productRepository;

        this.categoryRepository = categoryRepository;

        this.fileUploadService = fileUploadService;
    }


    // =====================================
    // ADD PRODUCT
    // =====================================

    @Override
    public Product addProduct(

            String name,

            String description,

            String brand,

            String price,

            Integer quantity,

            Long categoryId,

            MultipartFile image

    ) {

        if (productRepository.existsByNameAndDeletedFalse(name)) {

            throw new RuntimeException(
                    "Product already exists"
            );
        }


        Category category = categoryRepository

                .findByIdAndDeletedFalse(categoryId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );


        Product product = new Product();

        product.setName(name);

        product.setDescription(description);

        product.setBrand(brand);

        product.setPrice(
                new java.math.BigDecimal(price)
        );

        product.setQuantity(quantity);

        product.setCategory(category);

        product.setActive(true);

        product.setDeleted(false);


        // =====================================
        // PRODUCT IMAGE
        // =====================================

        if (image != null && !image.isEmpty()) {

            String imagePath =
                    fileUploadService.uploadProductImage(image);

            product.setImage(imagePath);
        }


        return productRepository.save(product);
    }


    // =====================================
    // GET ALL PRODUCTS
    // =====================================

    @Override
    public List<Product> getAllProducts() {

        return productRepository

                .findByDeletedFalse(
                        PageRequest.of(0, 100)
                )

                .getContent();
    }


    // =====================================
    // PAGINATION
    // =====================================

    @Override
    public Page<Product> getAllProducts(

            int page,

            int size

    ) {

        return productRepository.findByDeletedFalse(

                PageRequest.of(page, size)

        );
    }


    // =====================================
    // PAGINATION + SORTING
    // =====================================

    @Override
    public Page<Product> getAllProducts(

            Pageable pageable

    ) {

        return productRepository.findByDeletedFalse(
                pageable
        );
    }


    // =====================================
    // GET PRODUCT BY ID
    // =====================================

    @Override
    public Product getProductById(Long id) {

        return productRepository

                .findByIdAndDeletedFalse(id)

                .orElseThrow(

                        () -> new ResourceNotFoundException(
                                "Product not found"
                        )

                );
    }


    // =====================================
    // UPDATE PRODUCT
    // =====================================

    @Override
    public Product updateProduct(

            Long id,

            String name,

            String description,

            String brand,

            String price,

            Integer quantity,

            Long categoryId,

            MultipartFile image

    ) {

        System.out.println(
                "========== UPDATE PRODUCT =========="
        );

        System.out.println(
                "ID       : " + id
        );

        System.out.println(
                "Name     : " + name
        );

        System.out.println(
                "Brand    : " + brand
        );

        System.out.println(
                "Price    : " + price
        );

        System.out.println(
                "Quantity : " + quantity
        );

        System.out.println(
                "Category : " + categoryId
        );

        System.out.println(
                "===================================="
        );


        Product product =
                getProductById(id);


        product.setName(name);

        product.setDescription(description);

        product.setBrand(brand);

        product.setPrice(
                new java.math.BigDecimal(price)
        );

        product.setQuantity(quantity);


        Category category = categoryRepository

                .findByIdAndDeletedFalse(categoryId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );


        product.setCategory(category);


        // =====================================
        // UPDATE IMAGE
        // =====================================

        if (image != null && !image.isEmpty()) {

            String imagePath =
                    fileUploadService.uploadProductImage(image);

            product.setImage(imagePath);
        }


        return productRepository.save(product);
    }


    // =====================================
    // DELETE PRODUCT
    // SOFT DELETE
    // =====================================

    @Override
    public void deleteProduct(Long id) {

        Product product =
                getProductById(id);


        product.setDeleted(true);

        product.setActive(false);


        productRepository.save(product);
    }


    // =====================================
    // SEARCH PRODUCTS
    // =====================================

    @Override
    public Page<Product> searchProducts(

            String keyword,

            int page,

            int size

    ) {

        if (keyword == null) {

            keyword = "";
        }


        keyword = keyword.trim();


        return productRepository.searchProducts(

                keyword,

                PageRequest.of(page, size)

        );
    }


    // =====================================
    // SEARCH + SORTING
    // =====================================

    @Override
    public Page<Product> searchProducts(

            String keyword,

            Pageable pageable

    ) {

        if (keyword == null) {

            keyword = "";
        }


        keyword = keyword.trim();


        return productRepository.searchProducts(

                keyword,

                pageable

        );
    }


    // =====================================
    // CATEGORY FILTER
    // =====================================

    @Override
    public Page<Product> getProductsByCategory(

            Long categoryId,

            int page,

            int size

    ) {

        return productRepository.findByCategory(

                categoryId,

                PageRequest.of(page, size)

        );
    }


    // =====================================
    // CATEGORY + SORTING
    // =====================================

    @Override
    public Page<Product> getProductsByCategory(

            Long categoryId,

            Pageable pageable

    ) {

        return productRepository.findByCategory(

                categoryId,

                pageable

        );
    }


    // =====================================
    // SEARCH + CATEGORY + SORTING
    // =====================================

    @Override
    public Page<Product> searchByKeywordCategory(

            String keyword,

            Long categoryId,

            Pageable pageable

    ) {

        if (keyword == null) {

            keyword = "";
        }


        keyword = keyword.trim();


        return productRepository
                .searchByKeywordCategory(
                        keyword,
                        categoryId,
                        pageable
                );
    }


    // =====================================
    // TOTAL PRODUCTS
    // =====================================

    @Override
    public long getTotalProducts() {

        return productRepository.countByDeletedFalse();
    }


    // =====================================
    // PRODUCT COUNT
    // =====================================

    @Override
    public long getProductCount() {

        return productRepository.countByDeletedFalse();
    }

}