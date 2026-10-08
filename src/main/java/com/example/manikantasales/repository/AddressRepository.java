package com.example.manikantasales.repository;

import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // =====================================
    // GET ALL ADDRESSES OF USER
    // =====================================

    List<Address> findByUser(User user);

    // =====================================
    // GET DEFAULT ADDRESS
    // =====================================

    Optional<Address> findByUserAndDefaultAddressTrue(User user);

    // =====================================
    // DELETE ALL ADDRESSES OF USER
    // =====================================

    @Modifying
    @Query("DELETE FROM Address a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // =====================================
    // DELETE USER ADDRESS
    // =====================================

    void deleteByUser(User user);

    // =====================================
    // COUNT USER ADDRESSES
    // =====================================

    long countByUser(User user);

}