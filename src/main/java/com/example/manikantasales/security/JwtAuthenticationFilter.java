package com.example.manikantasales.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;



    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ){

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

    }






    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ){

        String path = request.getServletPath();


        return path.startsWith("/api/auth")
                ||
                path.startsWith("/css")
                ||
                path.startsWith("/js")
                ||
                path.startsWith("/images")
                ||
                path.startsWith("/uploads")
                ||
                path.startsWith("/assets")
                ||
                path.equals("/favicon.ico")
                ||
                request.getMethod().equalsIgnoreCase("OPTIONS");

    }








    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {



        String authHeader =
                request.getHeader("Authorization");


        System.out.println(
                "REQUEST : "
                + request.getRequestURI()
        );


        System.out.println(
                "AUTH HEADER : "
                + authHeader
        );



        if(authHeader == null ||
           !authHeader.startsWith("Bearer ")){

            System.out.println(
                    "NO JWT TOKEN"
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }





        try{


            String token =
                    authHeader.substring(7);



            System.out.println(
                    "TOKEN : "
                    + token
            );



            boolean valid =
                    jwtService.validateToken(token);



            System.out.println(
                    "TOKEN VALID : "
                    + valid
            );



            if(!valid){

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }






            String email =
                    jwtService.extractEmail(token);



            System.out.println(
                    "JWT EMAIL : "
                    + email
            );





            if(SecurityContextHolder
                    .getContext()
                    .getAuthentication()==null){



                UserDetails userDetails =
                        userDetailsService
                        .loadUserByUsername(email);





                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );




                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );




                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);





                System.out.println(
                        "SECURITY CONTEXT USER : "
                        +
                        SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
                );

            }



        }
        catch(Exception e){


            System.out.println(
                    "JWT ERROR : "
                    +e.getMessage()
            );

        }



        filterChain.doFilter(
                request,
                response
        );

    }}