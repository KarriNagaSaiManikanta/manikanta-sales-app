package com.example.manikantasales.config;


import com.example.manikantasales.security.CustomUserDetailsService;
import com.example.manikantasales.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
public class SecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;



    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ){

        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }






    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {



        http

        .csrf(csrf -> csrf.disable())



        .authorizeHttpRequests(auth -> auth





                // =============================
                // STATIC
                // =============================

                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/uploads/**",
                        "/assets/**",
                        "/favicon.ico"
                )
                .permitAll()






                // =============================
                // HTML PAGES
                // =============================

                .requestMatchers(
                        "/",
                        "/index.html",
                        "/login.html",
                        "/register.html",
                        "/forgot-password.html",
                        "/verify-otp.html",
                        "/reset-password.html",
                        "/profile.html",
                        "/cart.html",
                        "/checkout.html",
                        "/admin/**"
                )
                .permitAll()






                // =============================
                // AUTH
                // =============================

                .requestMatchers(
                        "/api/auth/**"
                )
                .permitAll()






                // =============================
                // USER PROFILE
                // =============================

                .requestMatchers(
                        "/api/users/profile/**"
                )
                .authenticated()







                // =============================
                // CATEGORY
                // =============================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/categories/**"
                )
                .permitAll()



                .requestMatchers(
                        "/api/categories/**"
                )
                .hasAuthority("ROLE_ADMIN")









                // =============================
                // PRODUCTS
                // =============================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/products/**"
                )
                .permitAll()



                .requestMatchers(
                        "/api/products/**"
                )
                .hasAuthority("ROLE_ADMIN")









                // =============================
                // CART
                // =============================

                .requestMatchers(
                        "/api/cart/**"
                )
                .authenticated()







                // =============================
                // WISHLIST
                // =============================

                .requestMatchers(
                        "/api/wishlist/**"
                )
                .authenticated()







                // =============================
                // ADDRESS API
                // =============================

                .requestMatchers(
                        "/api/address/**"
                )
                .authenticated()







                // =============================
                // USER ORDERS
                // =============================

                .requestMatchers(
                        "/api/orders/**"
                )
                .authenticated()







                // =============================
                // ADMIN
                // =============================

                .requestMatchers(
                        "/api/admin/**"
                )
                .hasAuthority("ROLE_ADMIN")







                // =============================
                // DEFAULT
                // =============================

                .anyRequest()
                .permitAll()



        )





        .userDetailsService(
                customUserDetailsService
        )





        .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        )





        .formLogin(
                login -> login.disable()
        )





        .httpBasic(
                basic -> basic.disable()
        );



        return http.build();

    }







    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }








    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    )
    throws Exception {


        return configuration.getAuthenticationManager();

    }



}