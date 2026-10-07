package com.example.manikantasales.controller;


import com.example.manikantasales.entity.User;
import com.example.manikantasales.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {



    private final UserService userService;



    public UserController(
            UserService userService
    ){

        this.userService = userService;

    }






    // =====================================
    // GET ALL USERS (ADMIN)
    // =====================================

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){


        return ResponseEntity.ok(
                userService.getAllUsers()
        );

    }







    // =====================================
    // TOTAL USER COUNT
    // =====================================

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount(){


        return ResponseEntity.ok(
                userService.getUserCount()
        );

    }








    // =====================================
    // GET SINGLE USER
    // =====================================

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(

            @PathVariable Long id

    ){


        return ResponseEntity.ok(
                userService.getUserById(id)
        );

    }









    // =====================================
    // BLOCK USER
    // =====================================

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockUser(

            @PathVariable Long id

    ){


        userService.blockUser(id);



        return ResponseEntity.ok(
                "User Blocked Successfully"
        );

    }









    // =====================================
    // UNBLOCK USER
    // =====================================

    @PutMapping("/unblock/{id}")
    public ResponseEntity<String> unblockUser(

            @PathVariable Long id

    ){


        userService.unblockUser(id);



        return ResponseEntity.ok(
                "User Unblocked Successfully"
        );

    }









    // =====================================
    // DELETE USER
    // =====================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(

            @PathVariable Long id

    ){


        userService.deleteUser(id);



        return ResponseEntity.ok(
                "User Deleted Successfully"
        );

    }


}