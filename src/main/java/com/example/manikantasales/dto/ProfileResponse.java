package com.example.manikantasales.dto;


public class ProfileResponse {


    private Long id;


    private String firstName;


    private String lastName;


    private String fullName;


    private String email;


    private String mobile;


    private String role;


    private boolean enabled;


    private String profileImage;




    public ProfileResponse() {

    }






    public Long getId() {

        return id;

    }


    public void setId(Long id) {

        this.id = id;

    }








    public String getFirstName() {

        return firstName;

    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;

    }








    public String getLastName() {

        return lastName;

    }


    public void setLastName(String lastName) {

        this.lastName = lastName;

    }








    public String getFullName() {

        return fullName;

    }


    public void setFullName(String fullName) {

        this.fullName = fullName;

    }








    public String getEmail() {

        return email;

    }


    public void setEmail(String email) {

        this.email = email;

    }








    public String getMobile() {

        return mobile;

    }


    public void setMobile(String mobile) {

        this.mobile = mobile;

    }








    public String getRole() {

        return role;

    }


    public void setRole(String role) {

        this.role = role;

    }








    public boolean isEnabled() {

        return enabled;

    }


    public void setEnabled(boolean enabled) {

        this.enabled = enabled;

    }








    public String getProfileImage() {

        return profileImage;

    }


    public void setProfileImage(String profileImage) {

        this.profileImage = profileImage;

    }



}