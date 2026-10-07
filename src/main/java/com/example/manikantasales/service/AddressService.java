package com.example.manikantasales.service;


import com.example.manikantasales.dto.AddressRequest;
import com.example.manikantasales.dto.AddressResponse;

import java.util.List;


public interface AddressService {



    // ADD ADDRESS
    AddressResponse addAddress(AddressRequest request);




    // GET LOGGED USER ADDRESSES
    List<AddressResponse> getMyAddresses();




    // GET ADDRESS BY ID
    AddressResponse getAddressById(Long id);




    // UPDATE ADDRESS
    AddressResponse updateAddress(
            Long id,
            AddressRequest request
    );




    // DELETE ADDRESS
    void deleteAddress(Long id);




    // SET DEFAULT ADDRESS
    AddressResponse setDefaultAddress(Long id);


}