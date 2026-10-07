package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.dto.AddressMapper;
import com.example.manikantasales.dto.AddressRequest;
import com.example.manikantasales.dto.AddressResponse;
import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.AddressRepository;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.AddressService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;



@Service
public class AddressServiceImpl implements AddressService {



    private final AddressRepository addressRepository;

    private final UserRepository userRepository;




    public AddressServiceImpl(
            AddressRepository addressRepository,
            UserRepository userRepository
    ){

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;

    }






    // =====================================
    // GET LOGGED USER
    // =====================================

    private User getLoggedInUser(){


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();



        if(authentication == null ||
           !authentication.isAuthenticated() ||
           authentication.getPrincipal().equals("anonymousUser")){


            throw new RuntimeException(
                    "User not authenticated"
            );

        }



        String email =
                authentication.getName();



        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found : "+email
                        )
                );

    }







    // =====================================
    // ADD ADDRESS
    // =====================================

    @Override
    public AddressResponse addAddress(
            AddressRequest request
    ){


        User user = getLoggedInUser();



        Address address =
                AddressMapper.toEntity(request);



        address.setUser(user);




        if(addressRepository.countByUser(user)==0){

            address.setDefaultAddress(true);

        }



        Address saved =
                addressRepository.save(address);



        return AddressMapper.toResponse(saved);

    }







    // =====================================
    // GET MY ADDRESSES
    // =====================================

    @Override
    public List<AddressResponse> getMyAddresses(){


        User user =
                getLoggedInUser();



        return addressRepository
                .findByUser(user)
                .stream()
                .map(AddressMapper::toResponse)
                .collect(Collectors.toList());

    }







    // =====================================
    // GET BY ID
    // =====================================

    @Override
    public AddressResponse getAddressById(Long id){


        Address address =
                addressRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Address not found"
                                )
                        );



        return AddressMapper.toResponse(address);

    }







    // =====================================
    // UPDATE
    // =====================================

    @Override
    public AddressResponse updateAddress(
            Long id,
            AddressRequest request
    ){


        Address address =
                addressRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Address not found"
                                )
                        );



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



        Address updated =
                addressRepository.save(address);



        return AddressMapper.toResponse(updated);

    }







    // =====================================
    // DELETE
    // =====================================

    @Override
    public void deleteAddress(Long id){


        Address address =
                addressRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Address not found"
                                )
                        );



        addressRepository.delete(address);

    }







    // =====================================
    // SET DEFAULT
    // =====================================

    @Override
    public AddressResponse setDefaultAddress(Long id){


        User user =
                getLoggedInUser();



        List<Address> addresses =
                addressRepository
                        .findByUser(user);



        for(Address address : addresses){

            address.setDefaultAddress(false);

        }



        Address selected =
                addressRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Address not found"
                                )
                        );



        selected.setDefaultAddress(true);



        addressRepository.saveAll(addresses);



        Address saved =
                addressRepository.save(selected);



        return AddressMapper.toResponse(saved);

    }

}