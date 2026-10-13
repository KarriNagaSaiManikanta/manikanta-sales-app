package com.example.manikantasales.config;

import com.example.manikantasales.security.CustomUserDetailsService;
import com.example.manikantasales.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
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

            // Disable CSRF because JWT authentication is stateless
            .csrf(csrf -> csrf.disable())


            // Enable CORS
            .cors(cors -> {})


            // JWT Stateless Session
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )


            .authorizeHttpRequests(auth -> auth



                // CORS OPTIONS REQUEST
                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                ).permitAll()



                // STATIC FILES
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/uploads/**",
                        "/assets/**",
                        "/favicon.ico"
                ).permitAll()



                // HTML PAGES
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
                ).permitAll()




                // AUTH APIs
                .requestMatchers(
                        "/api/auth/**"
                ).permitAll()




                // CATEGORY APIs

                // User can view categories
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/categories/**"
                ).permitAll()


                // Admin category management
                .requestMatchers(
                        "/api/categories/**"
                ).hasAuthority("ROLE_ADMIN")





                // PRODUCT APIs

                // User can view products
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/products/**"
                ).permitAll()


                // Admin product management
                .requestMatchers(
                        "/api/products/**"
                ).hasAuthority("ROLE_ADMIN")





                // USER AUTHENTICATED APIs

                .requestMatchers(
                        "/api/cart/**",
                        "/api/wishlist/**",
                        "/api/address/**",
                        "/api/orders/**",
                        "/api/users/profile/**"
                ).authenticated()





                // ADMIN APIs

                .requestMatchers(
                        "/api/admin/**"
                ).hasAuthority("ROLE_ADMIN")





                // OTHER REQUESTS

                .anyRequest().permitAll()

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
    ) throws Exception {

        return configuration.getAuthenticationManager();

    }


}