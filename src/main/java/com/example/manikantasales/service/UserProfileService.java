package com.example.manikantasales.service;


import com.example.manikantasales.dto.ProfileResponse;
import com.example.manikantasales.dto.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;



public interface UserProfileService {


    // =====================================
    // GET CURRENT USER PROFILE
    // =====================================

    ProfileResponse getCurrentUserProfile(
            String email
    );



    // =====================================
    // UPDATE PROFILE DETAILS
    // =====================================

    ProfileResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );



    // =====================================
    // UPLOAD PROFILE IMAGE
    // =====================================

    String uploadProfileImage(
            String email,
            MultipartFile file
    );


}