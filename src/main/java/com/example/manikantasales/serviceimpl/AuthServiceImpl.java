package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.dto.*;
import com.example.manikantasales.entity.PendingUser;
import com.example.manikantasales.entity.User;
import com.example.manikantasales.enums.Role;
import com.example.manikantasales.repository.PendingUserRepository;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.security.JwtService;
import com.example.manikantasales.service.AuthService;
import com.example.manikantasales.service.EmailService;
import com.example.manikantasales.service.OtpService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PendingUserRepository pendingUserRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private OtpService otpService;


    @Autowired
    private EmailService emailService; // Welcome Mail


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;



    // ===========================
    // USER REGISTER
    // ===========================

    @Override
    public String register(RegisterRequest request){


        if(userRepository.existsByEmail(request.getEmail())){
            return "Email already exists";
        }


        if(userRepository.existsByMobile(request.getMobile())){
            return "Mobile already exists";
        }


        pendingUserRepository
                .findByEmail(request.getEmail())
                .ifPresent(pendingUserRepository::delete);



        PendingUser pending = new PendingUser();


        pending.setFirstName(request.getFirstName());

        pending.setLastName(request.getLastName());

        pending.setEmail(request.getEmail());


        pending.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        pending.setMobile(request.getMobile());


        pending.setRole(Role.USER);



        String otp = otpService.generateOtp();


        pending.setOtp(otp);


        pending.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );



        pendingUserRepository.save(pending);



        otpService.sendRegistrationOtp(
                request.getEmail(),
                otp
        );


        return "OTP Sent Successfully";

    }





    // ===========================
    // ADMIN REGISTER WITH OTP
    // ===========================

    @Override
    public String adminRegister(RegisterRequest request){


        if(userRepository.existsByEmail(request.getEmail())){
            return "Email already exists";
        }


        if(userRepository.existsByMobile(request.getMobile())){
            return "Mobile already exists";
        }



        pendingUserRepository
                .findByEmail(request.getEmail())
                .ifPresent(pendingUserRepository::delete);



        PendingUser pending = new PendingUser();


        pending.setFirstName(request.getFirstName());

        pending.setLastName(request.getLastName());

        pending.setEmail(request.getEmail());


        pending.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        pending.setMobile(request.getMobile());


        pending.setRole(Role.ADMIN);



        String otp = otpService.generateOtp();


        pending.setOtp(otp);


        pending.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );



        pendingUserRepository.save(pending);



        otpService.sendRegistrationOtp(
                request.getEmail(),
                otp
        );



        return "Admin OTP Sent Successfully";

    }




 // ===========================
 // VERIFY OTP
 // ===========================

 @Override
 public String verifyOtp(VerifyOtpRequest request) {


     PendingUser pending = pendingUserRepository
             .findByEmail(request.getEmail())
             .orElseThrow(
                     () -> new RuntimeException(
                             "Registration not found"
                     )
             );



     // OTP Expiry Check
     if(pending.getOtpExpiry()
             .isBefore(LocalDateTime.now())){

         return "OTP Expired";

     }



     // OTP Match Check
     if(!pending.getOtp()
             .equals(request.getOtp())){

         return "Invalid OTP";

     }



     // ===========================
     // CREATE USER
     // ===========================

     User user = new User();



     user.setFirstName(
             pending.getFirstName()
     );


     user.setLastName(
             pending.getLastName()
     );


     user.setEmail(
             pending.getEmail()
     );


     user.setPassword(
             pending.getPassword()
     );


     user.setMobile(
             pending.getMobile()
     );



     // ===========================
     // ROLE COPY
     // USER / ADMIN
     // ===========================

     if(pending.getRole()!=null){

         user.setRole(
                 pending.getRole()
         );

     }
     else{

         user.setRole(
                 Role.USER
         );

     }



     user.setEnabled(true);

     user.setAccountLocked(false);



     userRepository.save(user);



     // Welcome Mail

     emailService.sendWelcomeEmail(
    	        user.getEmail(),
    	        user.getFirstName()
    	);



     // Remove Pending User

     pendingUserRepository.delete(pending);



     return "Registration Successful";

 }

    // ===========================
    // RESEND OTP
    // ===========================

    @Override
    public String resendOtp(ForgotPasswordRequest request){


        PendingUser pending =
                pendingUserRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "Registration not found"
                                )
                        );



        String otp =
                otpService.generateOtp();



        pending.setOtp(otp);


        pending.setOtpExpiry(
                LocalDateTime.now()
                        .plusMinutes(5)
        );


        pendingUserRepository.save(pending);



        otpService.sendRegistrationOtp(
                pending.getEmail(),
                otp
        );


        return "OTP Resent Successfully";

    }





    // ===========================
    // LOGIN
    // ===========================

    @Override
    public LoginResponse login(LoginRequest request){



        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );



        User user =
                userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(
                        ()->new RuntimeException(
                                "User Not Found"
                        )
                );



        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name()
                );



        return new LoginResponse(
                token,
                "Login Successful",
                user.getRole().name()
        );

    }





    // ===========================
    // FORGOT PASSWORD
    // ===========================

    @Override
    public String forgotPassword(ForgotPasswordRequest request){


        Optional<User> user =
                userRepository.findByEmail(
                        request.getEmail()
                );


        if(user.isEmpty()){

            return "Email not registered";

        }



        String otp =
                otpService.generateOtp();



        otpService.sendForgotPasswordOtp(
                request.getEmail(),
                otp
        );


        return "OTP Sent Successfully";

    }





    // ===========================
    // RESET PASSWORD
    // ===========================

    @Override
    public String resetPassword(
            ResetPasswordRequest request){


        String result =
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );


        if(!result.equals(
                "OTP Verified Successfully")){

            return result;

        }



        User user =
                userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(
                        ()->new RuntimeException(
                                "User not found"
                        )
                );



        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        userRepository.save(user);



        return "Password Updated Successfully";

    }

}