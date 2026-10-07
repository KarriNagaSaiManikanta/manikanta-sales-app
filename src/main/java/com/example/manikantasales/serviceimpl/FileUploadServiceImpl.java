package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.service.FileUploadService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.UUID;



@Service
public class FileUploadServiceImpl implements FileUploadService {



    @Value("${file.category-dir}")
    private String categoryDir;



    @Value("${file.product-dir}")
    private String productDir;







    // =====================================
    // COMMON FILE UPLOAD
    // =====================================

    private String uploadFile(

            MultipartFile file,

            String folder

    ){


        try {



            if(file == null || file.isEmpty()){


                throw new RuntimeException(
                        "Please select image"
                );


            }






            if(file.getSize() > 10 * 1024 * 1024){


                throw new RuntimeException(
                        "Image size should be below 10MB"
                );


            }







            String contentType =
                    file.getContentType();




            if(contentType == null ||

                    !contentType.startsWith("image/")){


                throw new RuntimeException(
                        "Only image files allowed"
                );


            }









            Path uploadPath =

                    Paths.get(

                            System.getProperty("user.dir"),

                            folder

                    );








            if(!Files.exists(uploadPath)){


                Files.createDirectories(
                        uploadPath
                );


            }









            String originalName =

                    file.getOriginalFilename();





            if(originalName == null){


                originalName = "image";


            }









            originalName =

                    originalName.replaceAll(

                            "[^a-zA-Z0-9.-]",

                            "_"

                    );









            String fileName =

                    UUID.randomUUID()

                    + "_"

                    + originalName;









            Path filePath =

                    uploadPath.resolve(
                            fileName
                    );










            Files.copy(

                    file.getInputStream(),

                    filePath,

                    StandardCopyOption.REPLACE_EXISTING

            );









            System.out.println(

                    "IMAGE SAVED : "

                    + filePath.toAbsolutePath()

            );









            return fileName;



        }

        catch(Exception e){


            throw new RuntimeException(

                    "Image upload failed : "

                    + e.getMessage()

            );


        }


    }












    // =====================================
    // CATEGORY IMAGE
    // =====================================


    @Override
    public String uploadCategoryImage(

            MultipartFile file

    ){


        String fileName =

                uploadFile(

                        file,

                        categoryDir

                );




        return "/uploads/categories/"
                +
                fileName;


    }









    // =====================================
    // PRODUCT IMAGE
    // =====================================


    @Override
    public String uploadProductImage(

            MultipartFile file

    ){



        String fileName =

                uploadFile(

                        file,

                        productDir

                );





        return "/uploads/products/"
                +
                fileName;



    }









    // =====================================
    // DELETE IMAGE
    // =====================================


    @Override
    public void deleteImage(

            String imagePath

    ){



        try {



            if(imagePath == null ||

                    imagePath.isEmpty()){


                return;


            }







            String fileName =

                    imagePath.substring(

                            imagePath.lastIndexOf("/") + 1

                    );








            String folder;





            if(imagePath.contains("/products/")){


                folder = productDir;


            }

            else{


                folder = categoryDir;


            }








            Path filePath =

                    Paths.get(

                            System.getProperty("user.dir"),

                            folder,

                            fileName

                    );










            if(Files.exists(filePath)){


                Files.delete(filePath);


                System.out.println(
                        "IMAGE DELETED : "
                        +
                        filePath
                );


            }





        }

        catch(Exception e){



            System.out.println(

                    "Delete image error : "

                    + e.getMessage()

            );


        }


    }



}