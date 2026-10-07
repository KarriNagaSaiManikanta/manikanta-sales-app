package com.example.manikantasales.controller;


import com.example.manikantasales.dto.ProfileResponse;
import com.example.manikantasales.dto.UpdateProfileRequest;
import com.example.manikantasales.service.UserProfileService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserProfileController {



    private final UserProfileService userProfileService;



    public UserProfileController(
            UserProfileService userProfileService
    ){

        this.userProfileService = userProfileService;

    }







    // =====================================
    // GET CURRENT USER PROFILE
    // =====================================

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getCurrentUserProfile(
            Authentication authentication
    ){


        String email =
                authentication.getName();



        ProfileResponse profile =
                userProfileService.getCurrentUserProfile(email);



        return ResponseEntity.ok(profile);

    }









    // =====================================
    // UPDATE PROFILE DETAILS
    // =====================================

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(

            Authentication authentication,

            @RequestBody UpdateProfileRequest request

    ){


        String email =
                authentication.getName();



        ProfileResponse updatedProfile =
                userProfileService.updateProfile(
                        email,
                        request
                );



        return ResponseEntity.ok(updatedProfile);

    }









    // =====================================
    // UPLOAD PROFILE IMAGE
    // =====================================

    @PostMapping("/profile/image")
    public ResponseEntity<String> uploadProfileImage(

            Authentication authentication,

            @RequestParam("file") MultipartFile file

    ){


        if(file == null || file.isEmpty()){


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please select image file");

        }




        String email =
                authentication.getName();




        String imagePath =
                userProfileService.uploadProfileImage(
                        email,
                        file
                );




        return ResponseEntity.ok(imagePath);

    }



}