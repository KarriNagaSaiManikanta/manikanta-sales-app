package com.example.manikantasales.config;

import com.example.manikantasales.entity.User;
import com.example.manikantasales.enums.Role;
import com.example.manikantasales.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByRole(Role.ADMIN)) {

            User admin = new User();

            admin.setFirstName("System");
            admin.setLastName("Admin");

            admin.setEmail("admin@manikantasales.com");
            admin.setMobile("9999999999");

            admin.setPassword(
                    passwordEncoder.encode("Admin@123")
            );

            admin.setRole(Role.ADMIN);

            admin.setEnabled(true);
            admin.setAccountLocked(false);

            userRepository.save(admin);

            System.out.println("==================================");
            System.out.println("DEFAULT ADMIN CREATED");
            System.out.println("Email : admin@manikantasales.com");
            System.out.println("Password : Admin@123");
            System.out.println("==================================");

        } else {

            System.out.println("Admin Already Exists.");

        }
    }
}