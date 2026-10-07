package com.example.manikantasales.service;


import org.springframework.web.multipart.MultipartFile;


public interface FileUploadService {


    // =====================================
    // CATEGORY IMAGE UPLOAD
    // =====================================

    String uploadCategoryImage(

            MultipartFile file

    );




    // =====================================
    // PRODUCT IMAGE UPLOAD
    // =====================================

    String uploadProductImage(

            MultipartFile file

    );




    // =====================================
    // DELETE IMAGE
    // =====================================

    void deleteImage(

            String imagePath

    );


}