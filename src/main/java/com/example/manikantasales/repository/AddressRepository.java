package com.example.manikantasales.repository;


import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



public interface AddressRepository 
        extends JpaRepository<Address, Long> {



    // Get all addresses of logged-in user
    List<Address> findByUser(User user);



    // Get default address
    Optional<Address> findByUserAndDefaultAddressTrue(
            User user
    );



    // Delete user address
    void deleteByUser(User user);



    // Count addresses
    long countByUser(User user);



}