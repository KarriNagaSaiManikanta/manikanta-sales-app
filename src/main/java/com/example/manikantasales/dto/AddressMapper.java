package com.example.manikantasales.dto;


import com.example.manikantasales.dto.AddressRequest;
import com.example.manikantasales.dto.AddressResponse;
import com.example.manikantasales.entity.Address;


public class AddressMapper {


    // ===============================
    // REQUEST DTO -> ENTITY
    // ===============================

    public static Address toEntity(AddressRequest request){


        Address address = new Address();


        address.setName(
                request.getName()
        );


        address.setMobile(
                request.getMobile()
        );


        address.setHouseNo(
                request.getHouseNo()
        );


        address.setStreet(
                request.getStreet()
        );


        address.setCity(
                request.getCity()
        );


        address.setState(
                request.getState()
        );


        address.setPincode(
                request.getPincode()
        );


        return address;
    }






    // ===============================
    // ENTITY -> RESPONSE DTO
    // ===============================

    public static AddressResponse toResponse(Address address){


        AddressResponse response = new AddressResponse();


        response.setId(
                address.getId()
        );


        response.setName(
                address.getName()
        );


        response.setMobile(
                address.getMobile()
        );


        response.setHouseNo(
                address.getHouseNo()
        );


        response.setStreet(
                address.getStreet()
        );


        response.setCity(
                address.getCity()
        );


        response.setState(
                address.getState()
        );


        response.setPincode(
                address.getPincode()
        );


        response.setDefaultAddress(
                address.isDefaultAddress()
        );


        response.setCreatedAt(
                address.getCreatedAt()
        );


        response.setUpdatedAt(
                address.getUpdatedAt()
        );


        return response;
    }

}