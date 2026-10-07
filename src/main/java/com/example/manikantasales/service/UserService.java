package com.example.manikantasales.service;


import com.example.manikantasales.entity.User;

import java.util.List;



public interface UserService {



    // =====================================
    // GET ALL USERS
    // =====================================

    List<User> getAllUsers();




    // =====================================
    // GET USER BY ID
    // =====================================

    User getUserById(Long id);




    // =====================================
    // GET USER BY EMAIL
    // =====================================

    User getUserByEmail(String email);




    // =====================================
    // TOTAL USER COUNT
    // =====================================

    Long getUserCount();




    // =====================================
    // BLOCK USER
    // =====================================

    void blockUser(Long id);




    // =====================================
    // UNBLOCK USER
    // =====================================

    void unblockUser(Long id);




    // =====================================
    // DELETE USER
    // =====================================

    void deleteUser(Long id);



}