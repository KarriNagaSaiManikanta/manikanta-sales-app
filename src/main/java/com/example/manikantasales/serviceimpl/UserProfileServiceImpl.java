package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.dto.ProfileResponse;
import com.example.manikantasales.dto.UpdateProfileRequest;
import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.UserProfileService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;



@Service
public class UserProfileServiceImpl implements UserProfileService {


    private final UserRepository userRepository;



    public UserProfileServiceImpl(
            UserRepository userRepository
    ){

        this.userRepository = userRepository;

    }





    // =====================================
    // GET CURRENT USER PROFILE
    // =====================================

    @Override
    public ProfileResponse getCurrentUserProfile(
            String email
    ){


        User user =
                userRepository.findByEmail(email)
                .orElseThrow(
                    () -> new RuntimeException(
                        "User not found"
                    )
                );


        return mapToResponse(user);

    }








    // =====================================
    // UPDATE PROFILE
    // =====================================

    @Override
    public ProfileResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ){


        User user =
                userRepository.findByEmail(email)
                .orElseThrow(
                    () -> new RuntimeException(
                        "User not found"
                    )
                );



        user.setFirstName(
                request.getFirstName()
        );


        user.setLastName(
                request.getLastName()
        );


        user.setMobile(
                request.getMobile()
        );



        userRepository.save(user);



        return mapToResponse(user);

    }









    // =====================================
    // UPLOAD PROFILE IMAGE
    // =====================================

    @Override
    public String uploadProfileImage(
            String email,
            MultipartFile file
    ){


        try {


            User user =
                    userRepository.findByEmail(email)
                    .orElseThrow(
                        () -> new RuntimeException(
                            "User not found"
                        )
                    );



            String uploadDir =
                    "uploads/profile/";



            File directory =
                    new File(uploadDir);



            if(!directory.exists()){

                directory.mkdirs();

            }



            String fileName =
                    UUID.randomUUID()
                    + "_"
                    + file.getOriginalFilename();




            Path path =
                    Paths.get(
                        uploadDir + fileName
                    );



            Files.write(
                    path,
                    file.getBytes()
            );




            String imagePath =
                    "/uploads/profile/"
                    + fileName;



            user.setProfileImage(
                    imagePath
            );



            userRepository.save(user);



            return imagePath;



        }
        catch(IOException e){


            throw new RuntimeException(
                    "Image upload failed"
            );


        }


    }









    // =====================================
    // MAP RESPONSE
    // =====================================

    private ProfileResponse mapToResponse(
            User user
    ){


        ProfileResponse response =
                new ProfileResponse();



        response.setId(
                user.getId()
        );



        response.setFirstName(
                user.getFirstName()
        );



        response.setLastName(
                user.getLastName()
        );



        response.setFullName(
                user.getFirstName()
                + " "
                + user.getLastName()
        );



        response.setEmail(
                user.getEmail()
        );



        response.setMobile(
                user.getMobile()
        );



        if(user.getRole()!=null){

            response.setRole(
                    user.getRole().name()
            );

        }



        response.setEnabled(
                user.isEnabled()
        );



        response.setProfileImage(
                user.getProfileImage()
        );



        return response;

    }


}