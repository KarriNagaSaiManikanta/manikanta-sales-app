package com.example.manikantasales.config;

import com.example.manikantasales.security.CustomUserDetailsService;
import com.example.manikantasales.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // Enable CORS configuration
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ALLOW ALL OPTIONS REQUESTS (CORS Preflight Check)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

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

                // AUTH APIs (Login, Register, etc.)
                .requestMatchers("/api/auth/**").permitAll()

                // CATEGORY APIs
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                .requestMatchers("/api/categories/**").hasAuthority("ROLE_ADMIN")

                // PRODUCT APIs
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers("/api/products/**").hasAuthority("ROLE_ADMIN")

                // SECURED APIs
                .requestMatchers(
                        "/api/cart/**",
                        "/api/wishlist/**",
                        "/api/address/**",
                        "/api/orders/**",
                        "/api/users/profile/**"
                ).authenticated()

                // ADMIN APIs
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                // DEFAULT
                .anyRequest().permitAll()
            )
            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable());

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