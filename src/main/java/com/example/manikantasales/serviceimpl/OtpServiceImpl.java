package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.entity.OtpToken;
import com.example.manikantasales.repository.OtpRepository;
import com.example.manikantasales.service.EmailService;
import com.example.manikantasales.service.OtpService;
import com.example.manikantasales.util.OtpGenerator;


import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;



@Service
public class OtpServiceImpl implements OtpService {



    private final OtpRepository otpRepository;

    private final EmailService emailService;





    public OtpServiceImpl(
            OtpRepository otpRepository,
            EmailService emailService
    ){

        this.otpRepository = otpRepository;
        this.emailService = emailService;

    }







    // =====================================
    // GENERATE OTP
    // =====================================

    @Override
    public String generateOtp(){


        return OtpGenerator.generateOtp();

    }








    // =====================================
    // SAVE OTP
    // =====================================

    @Override
    public void saveOtp(
            String email,
            String otp
    ){



        otpRepository
                .findByEmail(email)
                .ifPresent(
                        otpRepository::delete
                );




        OtpToken token =
                new OtpToken();



        token.setEmail(email);

        token.setOtp(otp);

        token.setVerified(false);


        token.setExpiryTime(
                LocalDateTime.now()
                .plusMinutes(5)
        );



        otpRepository.save(token);


    }









    // =====================================
    // REGISTRATION OTP
    // =====================================

    @Override
    public void sendRegistrationOtp(
            String email,
            String otp
    ){


        saveOtp(
                email,
                otp
        );


        emailService.sendOtpEmail(
                email,
                otp
        );


    }









    // =====================================
    // FORGOT PASSWORD OTP
    // =====================================

    @Override
    public void sendForgotPasswordOtp(
            String email,
            String otp
    ){


        saveOtp(
                email,
                otp
        );



        emailService.sendForgotPasswordOtp(
                email,
                otp
        );


    }









    // =====================================
    // VERIFY OTP
    // =====================================

    @Override
    public String verifyOtp(
            String email,
            String otp
    ){



        Optional<OtpToken> optional =

                otpRepository
                .findByEmailAndOtp(
                        email,
                        otp
                );





        if(optional.isEmpty()){


            return "Invalid OTP";

        }






        OtpToken token =
                optional.get();





        if(token.isVerified()){


            return "OTP Already Verified";

        }






        if(token.getExpiryTime()
                .isBefore(
                        LocalDateTime.now()
                )){


            return "OTP Expired";

        }







        token.setVerified(true);


        otpRepository.save(token);




        return "OTP Verified Successfully";


    }



}