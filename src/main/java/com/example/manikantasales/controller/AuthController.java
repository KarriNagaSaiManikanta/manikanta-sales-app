package com.example.manikantasales.controller;


import com.example.manikantasales.dto.ForgotPasswordRequest;
import com.example.manikantasales.dto.LoginRequest;
import com.example.manikantasales.dto.LoginResponse;
import com.example.manikantasales.dto.RegisterRequest;
import com.example.manikantasales.dto.ResetPasswordRequest;
import com.example.manikantasales.dto.VerifyOtpRequest;
import com.example.manikantasales.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {



    private final AuthService authService;



    public AuthController(AuthService authService){

        this.authService = authService;

    }





    // ==================================
    // TEST API
    // Check Controller Loading
    // ==================================

    @GetMapping("/test")
    public String test(){

        return "Auth Controller Working";

    }







    // ==================================
    // USER REGISTER
    // ==================================

    @PostMapping("/register")
    public String register(

            @Valid @RequestBody RegisterRequest request

    ){

        return authService.register(request);

    }







    // ==================================
    // ADMIN REGISTER
    // ==================================

    @PostMapping("/admin/register")
    public String adminRegister(

            @Valid @RequestBody RegisterRequest request

    ){

        return authService.adminRegister(request);

    }







    // ==================================
    // VERIFY REGISTER OTP
    // ==================================

    @PostMapping("/verify-otp")
    public String verifyOtp(

            @RequestBody VerifyOtpRequest request

    ){

        return authService.verifyOtp(request);

    }







    // ==================================
    // RESEND OTP
    // ==================================

    @PostMapping("/resend-otp")
    public String resendOtp(

            @RequestBody ForgotPasswordRequest request

    ){

        return authService.resendOtp(request);

    }







    // ==================================
    // LOGIN USER + ADMIN
    // ==================================

    @PostMapping("/login")
    public LoginResponse login(

            @Valid @RequestBody LoginRequest request

    ){

        return authService.login(request);

    }







    // ==================================
    // FORGOT PASSWORD
    // ==================================

    @PostMapping("/forgot-password")
    public String forgotPassword(

            @RequestBody ForgotPasswordRequest request

    ){

        return authService.forgotPassword(request);

    }







    // ==================================
    // RESET PASSWORD
    // ==================================

    @PostMapping("/reset-password")
    public String resetPassword(

            @RequestBody ResetPasswordRequest request

    ){

        return authService.resetPassword(request);

    }


}