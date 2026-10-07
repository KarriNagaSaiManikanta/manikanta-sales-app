package com.example.manikantasales.dto;


public class AddressRequest {


    private String name;

    private String mobile;

    private String houseNo;

    private String street;

    private String city;

    private String state;

    private String pincode;



    public AddressRequest() {

    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getHouseNo() {
        return houseNo;
    }


    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }



    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }



    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }



    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }



    public String getPincode() {
        return pincode;
    }


    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

}