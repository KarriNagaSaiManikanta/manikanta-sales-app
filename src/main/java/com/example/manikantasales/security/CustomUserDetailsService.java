package com.example.manikantasales.security;

import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class CustomUserDetailsService 
        implements UserDetailsService {


    private final UserRepository userRepository;


    public CustomUserDetailsService(
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {


        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found : " + email
                        )
                );


        return new org.springframework.security.core.userdetails.User(

                user.getEmail(),

                user.getPassword(),


                user.isEnabled(),          // enabled
                true,                       // account not expired
                true,                       // credentials not expired
                !user.isAccountLocked(),    // account not locked


                Collections.singleton(

                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )

                )

        );

    }

}